$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$tomcatHome = "C:\Users\shash\Tools\apache-tomcat-10.1.60"
$javaHome = "C:\Program Files\Microsoft\jdk-21.0.12.101-hotspot"
$warSource = Join-Path $projectRoot "target\shopsphere.war"
$warTarget = Join-Path $tomcatHome "webapps\shopsphere.war"
$appUrl = "http://localhost:8080/shopsphere/products"

Write-Host "Starting ShopSphere..." -ForegroundColor Cyan

if (-not (Test-Path -LiteralPath $javaHome)) {
    throw "Java was not found at: $javaHome"
}

if (-not (Test-Path -LiteralPath $tomcatHome)) {
    throw "Tomcat was not found at: $tomcatHome"
}

if (-not (Test-Path -LiteralPath $warSource)) {
    throw "WAR file was not found. Build first with: mvn clean package"
}

$mysqlService = Get-Service -Name "MySQL80" -ErrorAction SilentlyContinue
if ($null -eq $mysqlService) {
    Write-Warning "MySQL80 service was not found. Make sure MySQL Server is installed and running."
} elseif ($mysqlService.Status -ne "Running") {
    Write-Host "Starting MySQL80 service..."
    try {
        Start-Service -Name "MySQL80"
        Start-Sleep -Seconds 5
    } catch {
        Write-Warning "Could not start MySQL80 automatically. Start it manually or run this file as Administrator."
    }
}

$securePassword = Read-Host "Enter MySQL root password" -AsSecureString
$bstr = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($securePassword)
try {
    $plainPassword = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($bstr)
} finally {
    [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($bstr)
}

$env:JAVA_HOME = $javaHome
$env:CATALINA_HOME = $tomcatHome
$env:CATALINA_BASE = $tomcatHome
$env:ECOM_DB_URL = "jdbc:mysql://localhost:3306/ecommerce_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
$env:ECOM_DB_USER = "root"
$env:ECOM_DB_PASSWORD = $plainPassword

$webapps = Join-Path $tomcatHome "webapps"
$explodedApp = Join-Path $webapps "shopsphere"

if (-not (Test-Path -LiteralPath $warTarget) -or
    ((Get-Item -LiteralPath $warSource).LastWriteTime -gt (Get-Item -LiteralPath $warTarget).LastWriteTime)) {
    Write-Host "Deploying latest shopsphere.war..."
    if (Test-Path -LiteralPath $explodedApp) {
        Remove-Item -LiteralPath $explodedApp -Recurse -Force
    }
    Copy-Item -LiteralPath $warSource -Destination $warTarget -Force
}

$listener = Get-NetTCPConnection -LocalPort 8080 -State Listen -ErrorAction SilentlyContinue
if ($listener) {
    Write-Host "Port 8080 is already active. Opening ShopSphere..."
    Start-Process $appUrl
    Write-Host "URL: $appUrl" -ForegroundColor Green
    return
}

Start-Job -ScriptBlock {
    Start-Sleep -Seconds 12
    Start-Process "http://localhost:8080/shopsphere/products"
} | Out-Null

Write-Host "Tomcat is starting. Keep this window open while using ShopSphere." -ForegroundColor Yellow
Write-Host "URL: $appUrl" -ForegroundColor Green

Set-Location -LiteralPath (Join-Path $tomcatHome "bin")
& ".\catalina.bat" run
