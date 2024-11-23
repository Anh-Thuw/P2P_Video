CREATE TABLE users (
    user_id INT PRIMARY KEY,
    username VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    created_at TIMESTAMP,
    update_at TIMESTAMP
);

CREATE TABLE meets (
    meet_id INT PRIMARY KEY,
    host_user_id INT,
    room_code VARCHAR(255),
    created_at TIMESTAMP,
    update_at TIMESTAMP,
    status INT,
    FOREIGN KEY (host_user_id) REFERENCES users(user_id)
);

CREATE TABLE meet_participants (
    id INT PRIMARY KEY,
    meet_id INT,
    user_id INT,
    username VARCHAR(255),
    joined_at TIMESTAMP,
    left_at TIMESTAMP,
    FOREIGN KEY (meet_id) REFERENCES meets(meet_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
