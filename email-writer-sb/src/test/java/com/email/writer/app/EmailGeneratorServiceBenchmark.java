package com.email.writer.app;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode({Mode.AverageTime, Mode.Throughput})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@Threads(1)
public class EmailGeneratorServiceBenchmark {

    private EmailGeneratorService emailGeneratorService;
    private EmailRequest testEmailRequest;

    @Setup
    public void setup() {
        emailGeneratorService = new EmailGeneratorService(WebClient.builder());
        // Set required properties
        try {
            java.lang.reflect.Field apiUrlField = EmailGeneratorService.class.getDeclaredField("geminiApiUrl");
            java.lang.reflect.Field apiKeyField = EmailGeneratorService.class.getDeclaredField("geminiApiKey");
            
            apiUrlField.setAccessible(true);
            apiKeyField.setAccessible(true);
            
            apiUrlField.set(emailGeneratorService, "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=");
            apiKeyField.set(emailGeneratorService, System.getenv("AI_API_KEY"));
            
            testEmailRequest = new EmailRequest();
            testEmailRequest.setEmailContent("Hi, This is a test email for performance benchmarking. " +
                "Please provide a professional response.");
            testEmailRequest.setTone("professional");
        } catch (Exception e) {
            throw new RuntimeException("Failed to set up test environment", e);
        }
    }

    @Benchmark
    public String measureEmailGeneration() {
        return emailGeneratorService.generateEmailReply(testEmailRequest);
    }

    @Benchmark
    public void measureConcurrentRequests() throws InterruptedException {
        // Simulate 10 concurrent requests
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> emailGeneratorService.generateEmailReply(testEmailRequest));
            threads[i].start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(EmailGeneratorServiceBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}