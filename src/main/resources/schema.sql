CREATE TABLE IF NOT EXISTS post_feature (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    view_count    INTEGER NOT NULL,
    like_count    INTEGER NOT NULL,
    dislike_count INTEGER NOT NULL,
    comment_count INTEGER NOT NULL,
    content       TEXT NOT NULL,
    created_at    TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS labeled_post (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    feature_id    INTEGER NOT NULL,
    score         REAL NOT NULL,
    reasoning     TEXT NOT NULL,

    FOREIGN KEY(feature_id) REFERENCES post_feature(id)
);