package com.zeroads.data.heuristic

import android.util.Log

/**
 * AI-based Tracker Detection Engine.
 * Uses heuristic analysis to identify suspicious domain patterns.
 */
class HeuristicEngine {

    private val suspiciousKeywords = listOf(
        "track", "pixel", "analytics", "telemetry", "collect", "beacon", "measure", "metrics"
    )

    private val suspiciousPatterns = listOf(
        Regex(".*\\d{4,}.*"), // Domains with long numeric strings
        Regex(".*[a-zA-Z0-9]{32,}.*"), // Long hash-like subdomains
        Regex(".*-api\\..*"), // API endpoints often used for tracking
        Regex(".*-logs\\..*")  // Log endpoints
    )

    /**
     * Analyzes a domain and returns a risk score (0-100).
     */
    fun analyzeDomain(domain: String): Int {
        var score = 0
        
        // 1. Check for suspicious keywords
        suspiciousKeywords.forEach { keyword ->
            if (domain.contains(keyword, ignoreCase = true)) {
                score += 25
            }
        }

        // 2. Check for suspicious regex patterns
        suspiciousPatterns.forEach { pattern ->
            if (pattern.matches(domain)) {
                score += 30
            }
        }

        // 3. Check domain length (unusually long domains are suspicious)
        if (domain.length > 50) {
            score += 15
        }

        // 4. Check for multiple subdomains
        val subdomains = domain.split(".")
        if (subdomains.size > 4) {
            score += 20
        }

        return score.coerceAtMost(100)
    }

    /**
     * Determines if a domain should be blocked based on its risk score.
     */
    fun shouldBlock(domain: String, threshold: Int = 60): Boolean {
        val score = analyzeDomain(domain)
        Log.d("HeuristicEngine", "Domain: $domain, Risk Score: $score")
        return score >= threshold
    }
}
