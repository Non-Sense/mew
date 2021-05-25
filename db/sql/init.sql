CREATE SCHEMA IF NOT EXISTS `mewdb` DEFAULT CHARACTER SET utf8mb4;

CREATE TABLE IF NOT EXISTS `mewdb`.`user` (
    `user_id` INT NOT NULL AUTO_INCREMENT,
    `name_id` VARCHAR(45) NOT NULL,
    `name` VARCHAR(45) NOT NULL,
    `password` VARCHAR(45) NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT current_timestamp,
    `updated_at` TIMESTAMP NOT NULL DEFAULT current_timestamp on update current_timestamp,
    PRIMARY KEY (`user_id`),
    UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC) VISIBLE,
    UNIQUE INDEX `name_id_UNIQUE` (`name_id` ASC) VISIBLE)
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `mewdb`.`book` (
    `book_id` INT NOT NULL AUTO_INCREMENT,
    `user_id` INT NOT NULL,
    `title` VARCHAR(45) NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT current_timestamp,
    `updated_at` TIMESTAMP NOT NULL DEFAULT current_timestamp on update current_timestamp,
    PRIMARY KEY (`book_id`),
    UNIQUE INDEX `book_id_UNIQUE` (`book_id` ASC) VISIBLE,
    INDEX `user_idx` (`user_id` ASC) VISIBLE,
    FOREIGN KEY (`user_id`)
    REFERENCES `mewdb`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `mewdb`.`comment` (
    `comment_id` INT NOT NULL AUTO_INCREMENT,
    `book_id` INT NOT NULL,
    `user_id` INT NOT NULL,
    `rate` INT NULL,
    `comment` VARCHAR(255) NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT current_timestamp,
    PRIMARY KEY (`comment_id`),
    UNIQUE INDEX `comment_id_UNIQUE` (`comment_id` ASC) VISIBLE,
    INDEX `book_idx` (`book_id` ASC) VISIBLE,
    INDEX `user_idx` (`user_id` ASC) VISIBLE,
    FOREIGN KEY (`book_id`)
    REFERENCES `mewdb`.`book` (`book_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (`user_id`)
    REFERENCES `mewdb`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `mewdb`.`word` (
    `word_id` INT NOT NULL AUTO_INCREMENT,
    `book_id` INT NOT NULL,
    `word` VARCHAR(255) NOT NULL,
    `mean` VARCHAR(255) NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT current_timestamp,
    `updated_at` TIMESTAMP NOT NULL DEFAULT current_timestamp on update current_timestamp,
    PRIMARY KEY (`word_id`),
    UNIQUE INDEX `word_id_UNIQUE` (`word_id` ASC) VISIBLE,
    INDEX `book_idx` (`book_id` ASC) VISIBLE,
    FOREIGN KEY (`book_id`)
    REFERENCES `mewdb`.`book` (`book_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;

CREATE USER 'user' IDENTIFIED BY 'password';

GRANT SELECT, INSERT, TRIGGER, UPDATE, DELETE ON TABLE `mewdb`.* TO 'user';
