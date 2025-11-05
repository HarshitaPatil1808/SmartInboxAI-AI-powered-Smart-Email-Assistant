$url = "http://localhost:8080/api/email/generate"
$body = @{
    emailContent = "Dear team, I hope this email finds you well. I wanted to follow up on our last meeting about the project timeline."
    tone = "professional"
} | ConvertTo-Json

# Make 10 requests
for ($i = 1; $i -le 10; $i++) {
    Write-Host "Making request $i..."
    $response = Invoke-RestMethod -Uri $url -Method Post -Body $body -ContentType "application/json"
    Start-Sleep -Seconds 1
}

# Get metrics
$metrics = Invoke-RestMethod -Uri "http://localhost:8080/api/email/metrics" -Method Get
Write-Host "`nPerformance Metrics:"
$metrics | ConvertTo-Json