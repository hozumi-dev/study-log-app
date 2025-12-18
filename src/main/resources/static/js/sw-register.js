window.addEventListener("load", async () => {
    if(!("serviceWorker" in navigator)) return;

    try {
        const reg = await navigator.serviceWorker.register("/sw.js");
        console.log("SW register failed:", reg.scope);
    } catch(error) {
        console.warn("SW register failed:", error);
    }
});