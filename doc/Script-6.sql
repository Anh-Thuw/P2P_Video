CREATE TABLE users (
                       user_id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE meets (
                       meet_id INT AUTO_INCREMENT PRIMARY KEY,
                       host_user_id INT NOT NULL,
                       host_ip_address VARCHAR(45) NOT NULL,
                       room_code VARCHAR(255) UNIQUE NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       status INT NOT NULL DEFAULT 0,
                       FOREIGN KEY (host_user_id) REFERENCES users(user_id)
);

CREATE TABLE meet_participants (
                                   id INT AUTO_INCREMENT PRIMARY KEY,
                                   meet_id INT NOT NULL,
                                   user_id INT NOT NULL,
                                   joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   left_at TIMESTAMP DEFAULT NULL,
                                   FOREIGN KEY (meet_id) REFERENCES meets(meet_id),
                                   FOREIGN KEY (user_id) REFERENCES users(user_id)
);
