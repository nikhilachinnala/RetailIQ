# RetailIQ — Intelligent Retail Analytics Platform

A Java console-based starter project demonstrating six DSA modules through a simple Admin Portal and User Portal.
Built with standard Java only (Java 17+ recommended); no external libraries required.

## Run in Eclipse
1. Extract `RetailIQ.zip`.
2. In Eclipse: **File → Import → General → Existing Projects into Workspace → Next**.
3. Choose the extracted `RetailIQ` folder and click **Finish**.
4. Expand `src → retailiq`, right-click `Main.java` → **Run As → Java Application**.

If Eclipse doesn't recognize the project automatically:
- Create a Java Project named `RetailIQ`.
- Copy the extracted `src/retailiq` package into the project's `src` folder.
- Copy `data/products.csv` into the project root or leave it in the extracted folder; the starter app uses in-memory sample data.

## Portals
- **Admin Portal:** product list, add a product, sales summary, DSA Analytics Lab.
- **User Portal:** browse/search products, smart typo suggestions, budget selection, place a demo order, view order history.
- Demo orders are stored in memory and reset when the application closes. This is an educational prototype, not a production e-commerce system.

## Six DSA modules
1. **String Algorithms:** KMP, Z algorithm, Rabin–Karp.
2. **Suffix Structures:** suffix array and LCP array.
3. **Advanced Dynamic Programming:** edit distance, budget subset selection, matrix-chain multiplication.
4. **Network Flow:** Edmonds–Karp maximum flow and min-cut reachability.
5. **NP-Completeness & Approximation:** brute-force maximum clique for small graphs and a 2-approximation vertex cover.
6. **Randomized & Parallel Algorithms:** randomized quicksort, reservoir sampling, parallel reduction.

The modules are implemented as working educational demonstrations. Some advanced concepts (e.g., SAT/3-SAT, suffix automata, Dinic, Miller–Rabin) are not included in this starter version and can be added as extensions.
