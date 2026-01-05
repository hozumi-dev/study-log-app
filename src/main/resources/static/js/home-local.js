import { addLog, listByDate, sumSecondsByDate } from "./local-db.js";

function formatHms(totalSec) {
  const sec = Number(totalSec) || 0;
  const h = Math.floor(sec / 3600);
  const m = Math.floor((sec % 3600) / 60);
  const s = sec % 60;
  return `${h}:${String(m).padStart(2,"0")}:${String(s).padStart(2,"0")}`;
}

async function refreshToday(isoDate) {
  const total = await sumSecondsByDate(isoDate);
  const totalEl = document.getElementById("totalHms");
  if (totalEl) totalEl.textContent = formatHms(total);

  const logs = await listByDate(isoDate);
  const tbody = document.getElementById("todayLogsBody");
  if (!tbody) return;

  tbody.innerHTML = logs
    .slice().reverse()
    .map(l => `
      <tr>
        <td>${l.studyDate}</td>
        <td>${l.subject ?? ""}</td>
        <td>${Math.floor((Number(l.seconds)||0)/60)}</td>
        <td>${l.memo ?? ""}</td>
      </tr>
    `)
    .join("");
}

document.addEventListener("DOMContentLoaded", async () => {
  const isoDate = document.body.dataset.today;
  if (!isoDate) return;

  // 初期表示（IndexedDB → 画面）
  await refreshToday(isoDate);

  // フォーム送信を横取りしてローカル保存
  const form = document.getElementById("logForm");
  if (!form) return;

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const subject = document.getElementById("subject")?.value ?? "";
    const memo = document.getElementById("memo")?.value ?? "";
    const seconds = Number(document.getElementById("secondsInput")?.value ?? 0);

    await addLog({
      studyDate: isoDate,
      subject,
      memo,
      seconds,
      createdAt: new Date().toISOString()
    });

    // 入力リセット
    form.reset();
    document.getElementById("minutesPreview").textContent = "0";
    document.getElementById("minutesInput").value = "";
    document.getElementById("secondsInput").value = "";

    await refreshToday(isoDate);
  });
});