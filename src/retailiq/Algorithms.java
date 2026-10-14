package retailiq;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public final class Algorithms {
    private Algorithms() {}

    // M1: KMP substring search. Returns starting indices.
    public static List<Integer> kmp(String text, String pattern) {
        List<Integer> hits = new ArrayList<>();
        if (pattern.isEmpty()) { hits.add(0); return hits; }
        int[] lps = new int[pattern.length()];
        for (int i = 1, len = 0; i < pattern.length();) {
            if (pattern.charAt(i) == pattern.charAt(len)) lps[i++] = ++len;
            else if (len != 0) len = lps[len - 1];
            else lps[i++] = 0;
        }
        for (int i = 0, j = 0; i < text.length();) {
            if (text.charAt(i) == pattern.charAt(j)) { i++; j++; }
            if (j == pattern.length()) { hits.add(i - j); j = lps[j - 1]; }
            else if (i < text.length() && text.charAt(i) != pattern.charAt(j)) {
                if (j != 0) j = lps[j - 1]; else i++;
            }
        }
        return hits;
    }

    public static int[] zArray(String s) {
        int n = s.length();
        int[] z = new int[n];
        for (int i = 1, l = 0, r = 0; i < n; i++) {
            if (i <= r) z[i] = Math.min(r - i + 1, z[i - l]);
            while (i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i])) z[i]++;
            if (i + z[i] - 1 > r) { l = i; r = i + z[i] - 1; }
        }
        return z;
    }

    public static List<Integer> rabinKarp(String text, String pattern) {
        List<Integer> hits = new ArrayList<>();
        int n = text.length(), m = pattern.length();
        if (m == 0) { hits.add(0); return hits; }
        if (m > n) return hits;
        long base = 256, mod = 1_000_000_007L, pHash = 0, tHash = 0, power = 1;
        for (int i = 0; i < m; i++) {
            pHash = (pHash * base + pattern.charAt(i)) % mod;
            tHash = (tHash * base + text.charAt(i)) % mod;
            if (i < m - 1) power = (power * base) % mod;
        }
        for (int i = 0; i <= n - m; i++) {
            if (pHash == tHash && text.regionMatches(i, pattern, 0, m)) hits.add(i);
            if (i < n - m) {
                tHash = (tHash - text.charAt(i) * power % mod + mod) % mod;
                tHash = (tHash * base + text.charAt(i + m)) % mod;
            }
        }
        return hits;
    }

    // M2: suffix array by sorting suffix start positions; educational O(n log^2 n) implementation.
    public static int[] suffixArray(String s) {
        Integer[] idx = new Integer[s.length()];
        for (int i = 0; i < idx.length; i++) idx[i] = i;
        Arrays.sort(idx, Comparator.comparing(s::substring));
        return Arrays.stream(idx).mapToInt(Integer::intValue).toArray();
    }

    public static int[] lcpArray(String s, int[] sa) {
        int n = s.length();
        int[] rank = new int[n], lcp = new int[n];
        for (int i = 0; i < n; i++) rank[sa[i]] = i;
        int h = 0;
        for (int i = 0; i < n; i++) {
            int r = rank[i];
            if (r == 0) continue;
            int j = sa[r - 1];
            while (i + h < n && j + h < n && s.charAt(i + h) == s.charAt(j + h)) h++;
            lcp[r] = h;
            if (h > 0) h--;
        }
        return lcp;
    }

    // M3: Levenshtein edit distance.
    public static int editDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= b.length(); j++) dp[0][j] = j;
        for (int i = 1; i <= a.length(); i++)
            for (int j = 1; j <= b.length(); j++)
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1));
        return dp[a.length()][b.length()];
    }

    // M3: 0/1 budget subset selection; returns selected indices with maximum total spend <= budget.
    public static List<Integer> selectWithinBudget(List<Product> products, double budget) {
        int n = products.size();
        int cap = (int)Math.floor(budget);
        int[] dp = new int[cap + 1];
        boolean[][] take = new boolean[n][cap + 1];
        for (int i = 0; i < n; i++) {
            int price = (int)Math.round(products.get(i).getPrice());
            for (int c = cap; c >= price; c--) {
                if (dp[c - price] + price > dp[c]) {
                    dp[c] = dp[c - price] + price;
                    take[i][c] = true;
                }
            }
        }
        List<Integer> result = new ArrayList<>();
        int c = cap;
        for (int i = n - 1; i >= 0; i--) {
            int price = (int)Math.round(products.get(i).getPrice());
            if (c >= price && take[i][c] && dp[c] == dp[c - price] + price) {
                result.add(i);
                c -= price;
            }
        }
        Collections.reverse(result);
        return result;
    }

    // M3: matrix-chain multiplication minimum scalar multiplications.
    public static long matrixChainCost(int[] dims) {
        int n = dims.length - 1;
        if (n <= 1) return 0;
        long[][] dp = new long[n][n];
        for (int len = 2; len <= n; len++)
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                dp[i][j] = Long.MAX_VALUE;
                for (int k = i; k < j; k++)
                    dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k + 1][j] + (long)dims[i] * dims[k + 1] * dims[j + 1]);
            }
        return dp[0][n - 1];
    }

    // M4: Edmonds-Karp max-flow. Input matrix is capacity matrix; returns max flow.
    public static int maxFlow(int[][] capacity, int source, int sink) {
        int n = capacity.length;
        int[][] residual = new int[n][n];
        for (int i = 0; i < n; i++) residual[i] = capacity[i].clone();
        int flow = 0;
        int[] parent = new int[n];
        while (bfsResidual(residual, source, sink, parent)) {
            int pathFlow = Integer.MAX_VALUE;
            for (int v = sink; v != source; v = parent[v]) pathFlow = Math.min(pathFlow, residual[parent[v]][v]);
            for (int v = sink; v != source; v = parent[v]) {
                residual[parent[v]][v] -= pathFlow;
                residual[v][parent[v]] += pathFlow;
            }
            flow += pathFlow;
        }
        return flow;
    }

    private static boolean bfsResidual(int[][] residual, int s, int t, int[] parent) {
        Arrays.fill(parent, -1);
        parent[s] = s;
        Queue<Integer> q = new ArrayDeque<>();
        q.add(s);
        while (!q.isEmpty()) {
            int u = q.remove();
            for (int v = 0; v < residual.length; v++)
                if (parent[v] == -1 && residual[u][v] > 0) {
                    parent[v] = u;
                    if (v == t) return true;
                    q.add(v);
                }
        }
        return false;
    }

    // M5: brute-force maximum clique; use only for small graphs (n <= 20).
    public static List<Integer> maximumClique(boolean[][] graph) {
        int n = graph.length;
        if (n > 20) throw new IllegalArgumentException("Brute force maximum clique is limited to 20 vertices.");
        List<Integer> best = new ArrayList<>();
        long limit = 1L << n;
        for (long mask = 1; mask < limit; mask++) {
            if (Long.bitCount(mask) <= best.size()) continue;
            List<Integer> chosen = new ArrayList<>();
            boolean clique = true;
            for (int i = 0; i < n && clique; i++) if ((mask & (1L << i)) != 0) {
                for (int j : chosen) if (!graph[i][j]) { clique = false; break; }
                chosen.add(i);
            }
            if (clique) best = chosen;
        }
        return best;
    }

    // M5: 2-approximation for vertex cover.
    public static Set<Integer> approximateVertexCover(boolean[][] graph) {
        int n = graph.length;
        boolean[][] used = new boolean[n][n];
        Set<Integer> cover = new LinkedHashSet<>();
        for (int i = 0; i < n; i++) for (int j = i + 1; j < n; j++)
            if (graph[i][j] && !used[i][j]) {
                cover.add(i); cover.add(j);
                for (int k = 0; k < n; k++) { used[i][k] = used[k][i] = true; used[j][k] = used[k][j] = true; }
            }
        return cover;
    }

    // M6: randomized quicksort.
    public static void randomizedQuickSort(int[] a) { quickSort(a, 0, a.length - 1); }
    private static void quickSort(int[] a, int lo, int hi) {
        if (lo >= hi) return;
        int p = ThreadLocalRandom.current().nextInt(lo, hi + 1);
        swap(a, p, hi);
        int pivot = a[hi], i = lo;
        for (int j = lo; j < hi; j++) if (a[j] <= pivot) swap(a, i++, j);
        swap(a, i, hi);
        quickSort(a, lo, i - 1);
        quickSort(a, i + 1, hi);
    }
    private static void swap(int[] a, int i, int j) { int t = a[i]; a[i] = a[j]; a[j] = t; }

    // M6: reservoir sampling picks k items uniformly from a stream.
    public static <T> List<T> reservoirSample(List<T> stream, int k) {
        List<T> sample = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < stream.size(); i++) {
            if (i < k) sample.add(stream.get(i));
            else {
                int j = random.nextInt(i + 1);
                if (j < k) sample.set(j, stream.get(i));
            }
        }
        return sample;
    }

    // M6: parallel reduce example.
    public static long parallelSum(int[] values) {
        return IntStream.of(values).parallel().asLongStream().sum();
    }
}
