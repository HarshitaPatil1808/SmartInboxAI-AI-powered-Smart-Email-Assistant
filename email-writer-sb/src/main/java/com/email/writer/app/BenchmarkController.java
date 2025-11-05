package com.email.writer.app;

import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/benchmark")
@CrossOrigin(origins = "*")
public class BenchmarkController {
    private static final Logger logger = LoggerFactory.getLogger(BenchmarkController.class);
    private final EmailGeneratorService reactiveService;
    private final BlockingEmailGeneratorService blockingService;

    public BenchmarkController(EmailGeneratorService reactiveService, BlockingEmailGeneratorService blockingService) {
        this.reactiveService = reactiveService;
        this.blockingService = blockingService;
    }

    @PostMapping("/compare")
    public Map<String, Object> compareBoth(@RequestBody EmailRequest request, @RequestParam(defaultValue = "10") int iterations) {
        Map<String, Object> results = new HashMap<>();
        
        // Test blocking service
        long blockingStart = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            blockingService.generateEmailReply(request);
        }
        long blockingTotal = System.currentTimeMillis() - blockingStart;
        
        // Test reactive service
        long reactiveStart = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            reactiveService.generateEmailReply(request);
        }
        long reactiveTotal = System.currentTimeMillis() - reactiveStart;
        
        // Calculate improvement percentage
        double improvementPercentage = ((double)(blockingTotal - reactiveTotal) / blockingTotal) * 100;
        
        results.put("blockingTotalTime", blockingTotal);
        results.put("blockingAverageTime", blockingTotal / iterations);
        results.put("reactiveTotalTime", reactiveTotal);
        results.put("reactiveAverageTime", reactiveTotal / iterations);
        results.put("improvementPercentage", improvementPercentage);
        
        logger.info("Benchmark results: Blocking total={}ms, Reactive total={}ms, Improvement={}%",
            blockingTotal, reactiveTotal, String.format("%.2f", improvementPercentage));
            
        return results;
    }

    @PostMapping("/concurrent")
    public Map<String, Object> concurrentBenchmark(@RequestBody EmailRequest request, 
                                                 @RequestParam(defaultValue = "10") int concurrentRequests) {
        Map<String, Object> results = new HashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(concurrentRequests);
        
        // Test blocking service
        long blockingStart = System.currentTimeMillis();
        CompletableFuture<?>[] blockingFutures = IntStream.range(0, concurrentRequests)
            .mapToObj(i -> CompletableFuture.runAsync(
                () -> blockingService.generateEmailReply(request), executor))
            .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(blockingFutures).join();
        long blockingTotal = System.currentTimeMillis() - blockingStart;
        
        // Test reactive service
        long reactiveStart = System.currentTimeMillis();
        CompletableFuture<?>[] reactiveFutures = IntStream.range(0, concurrentRequests)
            .mapToObj(i -> CompletableFuture.runAsync(
                () -> reactiveService.generateEmailReply(request), executor))
            .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(reactiveFutures).join();
        long reactiveTotal = System.currentTimeMillis() - reactiveStart;
        
        executor.shutdown();
        
        // Calculate improvement percentage for concurrent requests
        double improvementPercentage = ((double)(blockingTotal - reactiveTotal) / blockingTotal) * 100;
        
        results.put("concurrentRequests", concurrentRequests);
        results.put("blockingTotalTime", blockingTotal);
        results.put("blockingAverageTime", blockingTotal / concurrentRequests);
        results.put("reactiveTotalTime", reactiveTotal);
        results.put("reactiveAverageTime", reactiveTotal / concurrentRequests);
        results.put("improvementPercentage", improvementPercentage);
        
        logger.info("Concurrent benchmark results: Blocking total={}ms, Reactive total={}ms, Improvement={}%",
            blockingTotal, reactiveTotal, String.format("%.2f", improvementPercentage));
            
        return results;
    }
}