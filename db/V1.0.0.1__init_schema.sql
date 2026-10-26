CREATE TABLE IF NOT EXISTS categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL,
    operation ENUM('EXPENSE', 'INCOME') NOT NULL,
    color VARCHAR(7) NOT NULL,
    icon VARCHAR(64),
    ordinal INT NOT NULL DEFAULT 0
);

CREATE UNIQUE INDEX ux_categories_operation_name ON categories (operation, name);

CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    issued DATE NOT NULL,
    amount DECIMAL(19, 5) NOT NULL,
    `description` VARCHAR(255),
    operation ENUM('EXPENSE', 'INCOME') NOT NULL,
    category_id BIGINT NOT NULL,
    currency VARCHAR(3) NOT NULL,
    CONSTRAINT category_transactions_fk FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE RESTRICT ON UPDATE CASCADE
);
