CREATE TABLE IF NOT EXISTS currencies (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    code TEXT CHECK ( length(code) == 3 ) UNIQUE,
    name TEXT CHECK ( length(name) BETWEEN 3 AND 40),
    sign TEXT NOT NULL CHECK ( length(sign) BETWEEN 1 AND 4)
);
