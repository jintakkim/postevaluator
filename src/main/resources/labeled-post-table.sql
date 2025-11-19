CREATE TABLE IF NOT EXISTS labeled_post (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    post_id       INTEGER NOT NULL,
    score         REAL NOT NULL,
    reasoning     TEXT NOT NULL,

    FOREIGN KEY(post_id) REFERENCES post(id) ON DELETE CASCADE
);