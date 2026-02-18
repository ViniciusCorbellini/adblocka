# Adblocka

> Want a break from the ads?

Adblocka is an experimental, educational ad-blocking proxy written in Java. The goal of the project is to deeply understand how HTTP/HTTPS traffic works, how ad blockers operate at the network level, and how filtering, tunneling, and request inspection can be implemented from scratch.  

---

## 🚀 Project Goals

* Implement an HTTP proxy capable of intercepting and forwarding requests
* Support HTTPS traffic via the `CONNECT` tunneling mechanism
* Inspect and filter requests based on configurable rules
* Understand how ad blockers work beyond browser extensions
* Practice low-level networking with Java (Sockets, Threads, etc.)
* Build a configurable CLI-driven tool

---

## 🗂️ Project Structure (Simplified)

```
adblocka/
├── core/            # Core proxy logic
└── App.java         # Application entry point
```

> Structure will evolve as the project grows.

---

## ⚙️ Features (Current & Planned)

### Implemented / In Progress

* HTTP proxy server
* Basic request parsing
* HTTPS tunneling via CONNECT
* Multithreaded client handling
* DNS-level request blocking with pluggable blocklists

### Planned

* Rule-based request filtering (domains, paths, headers)
* External blocklist providers
* Configurable blocklists
* CLI flags for behavior control
* Logging and metrics
* Performance tuning (buffers, pooling)
* HTTPS request manipulation

---

## 📦 Requirements

* Java 17+
* Maven
* Basic knowledge of networking concepts (HTTP, TCP, TLS)
* Make (optional)

---

## ▶️ Running the Project

```bash
# read the Makefile for more information
make up
```

Then configure your browser or system to use `localhost:8080` as an HTTP/HTTPS proxy.

---

## 🧪 Status

Early-stage development. Expect breaking changes and refactors.