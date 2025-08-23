CREATE TRIGGER auto_naming_me BEFORE INSERT ON monthly_expenses
FOR EACH ROW
BEGIN
    DECLARE count_me BIGINT;

    SET @count_me := (
        SELECT COUNT(*) FROM monthly_expenses
    );

    SET NEW.name = CONCAT('ME-', LPAD(@count_me, 7, 0)); 
END