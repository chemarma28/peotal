CREATE TABLE IF NOT EXISTS categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    issued DATE NOT NULL,
    amount DECIMAL(19, 5) NOT NULL,
    `description` VARCHAR(255) DEFAULT '',
    operation ENUM('EXPENSE', 'INCOME'),
    category_id BIGINT,
    CONSTRAINT category_transactions_fk FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE RESTRICT ON UPDATE CASCADE
)