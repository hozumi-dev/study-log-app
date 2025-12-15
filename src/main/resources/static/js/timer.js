let startMs = null;
let intervalId = null;
let accumulatedMs = 0;

function format(ms) {
    const totalSec = Math.floor(ms / 1000);
    const h = String(Math.floor(totalSec / 3600));
    const m = String(Math.floor((totalSec % 3600) / 60)).padStart(2, "0");
    const s = String(totalSec % 60).padStart(2, "0");
    return `${h}:${m}:${s}`; 
}

function startTimer() {
    if (intervalId) return;
    startMs = Date.now();
    intervalId = setInterval(() => {
        const elapsed = Date.now() - startMs;
        document.getElementById('timer').textContent = format(elapsed);
    }, 200);
}

function stopTimer() {
    if(!intervalId) return;
    clearInterval(intervalId);
    intervalId = null;

    const elapsedMs = Date.now() - startMs;

    const seconds = Math.max(0, Math.floor(elapsedMs / 1000));
    document.getElementById('secondsInput').value = seconds;
    
    const minutes = Math.max(0, Math.floor(elapsedMs / 60000));
    document.getElementById('minutesInput').value = minutes;
    document.getElementById('minutesPreview').textContent = minutes;
}