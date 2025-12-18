const VERSION = "v1";
const STATIC_CACHE = `static-${VERSION}`;
const DYNAMIC_CACHE = `dynamic-${VERSION}`;

const PRECACHE_URLS = [
    "/",
    "/manifest.webmanifest",
    "/css/app.css",
    "/css/sidebar.css",
    "/js/timer.js",
    "/js/sw-register.js",
    "/icons/icon-192.png",
    "/icons/icon-512.png",
    "/offline.html"
];

self.addEventListener("install", (event) => {
    event.waitUntil(
        caches.open(STATIC_CACHE).then((cache) => cache.addAll(PRECACHE_URLS))
    );
    self.skipWaiting();
});

self.addEventListener("activate", (event) => {
    event.waitUntil(
        caches.keys().then((keys) =>
            Promise.all(
                keys
                    .filter((k) => k.startsWith("static-") || k.startsWith("dynamic-"))
                    .filter((k) => k !== STATIC_CACHE && k !== DYNAMIC_CACHE)
                    .map((k) => caches.delete(k))
            )
        )
    );
    self.clients.claim();
});

self.addEventListener("fetch", (event) => {
    const req = event.request;
    const url = new URL(req.url);

    //同一オリジン以外触らない
    if (url.origin !== self.location.origin) return;

    //ページ遷移はネット優先→ダメならキャッシュ/オフライン
    if(req.mode === "navigate") {
        event.respondWith(
            fetch(req)
            .then((res) => {
                const copy = res.clone();
                caches.open(DYNAMIC_CACHE).then((cache) => cache.put(req, copy));
                return res;
            })
            .catch(async () =>  {
                const cached = await caches.match(req);
                return cached || (await caches.match("/")) || (await caches.match("/offline.html"));
            })
        );
        return;
    }

    event.respondWith(
        caches.match(req).then((cached) => {
            if (cached) return cached;
            return fetch(req).then((res) => {
                const copy = res.clone();
                caches.open(DYNAMIC_CACHE).then((cache) => cache.put(req, copy));
            return res;
            });
        })
    );
});