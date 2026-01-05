const DB_NAME = "studylog_db";
const DB_VERSION = 1;
const STORE = "logs";

function openDb() {
    return new Promise((resolve, reject) => {
        const req = indexedDB.open(DB_NAME, DB_VERSION);

        req.onupgradeneeded = () => {
            const db = req.result;
            if(!db.objectStoreNames.contains(STORE)) {
                const store = db.createObjectStore(STORE, { keyPath: "id", autoIncrement: true});
                store.createIndex("studyDate", "studyDate", { unique: false});
            }
        };

        req.onsuccess = () => resolve(req,result);
        req.onerror = () => reject(req.error);
    });
}

export async function addLog(log) {
  const db = await openDb();
  return new Promise((resolve, reject) => {
    const tx = db.transaction(STORE, "readwrite");
    tx.objectStore(STORE).add(log);
    tx.oncomplete = () => resolve(true);
    tx.onerror = () => reject(tx.error);
  });
}

export async function listByDate(isoDate) {
  const db = await openDb();
  return new Promise((resolve, reject) => {
    const tx = db.transaction(STORE, "readonly");
    const index = tx.objectStore(STORE).index("studyDate");
    const req = index.getAll(isoDate);
    req.onsuccess = () => resolve(req.result ?? []);
    req.onerror = () => reject(req.error);
  });
}

export async function sumSecondsByDate(isoDate) {
  const logs = await listByDate(isoDate);
  return logs.reduce((acc, x) => acc + (Number(x.seconds) || 0), 0);
}