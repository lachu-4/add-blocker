package com.zeroads.data.filter

import java.util.*

/**
 * High-performance Trie-based domain filter for ad blocking.
 * Optimized for O(L) lookup where L is the length of the domain.
 */
class AdDomainFilter {

    private val root = TrieNode()

    fun addDomain(domain: String) {
        var current = root
        val parts = domain.lowercase(Locale.ROOT).split('.').reversed()
        for (part in parts) {
            current = current.children.getOrPut(part) { TrieNode() }
        }
        current.isEndOfDomain = true
    }

    fun isBlocked(domain: String): Boolean {
        var current = root
        val parts = domain.lowercase(Locale.ROOT).split('.').reversed()
        for (part in parts) {
            current = current.children[part] ?: return false
            if (current.isEndOfDomain) return true
        }
        return false
    }

    private class TrieNode {
        val children = mutableMapOf<String, TrieNode>()
        var isEndOfDomain = false
    }
}
