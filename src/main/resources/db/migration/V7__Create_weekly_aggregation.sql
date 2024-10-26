CREATE MATERIALIZED VIEW transaction_weekly_summary
            WITH (timescaledb.continuous) AS
SELECT
    time_bucket('1 week', date) AS week,
    user_id,
    category,
    SUM(CASE WHEN amount > 0 THEN amount ELSE 0 END) AS total_income,
    SUM(CASE WHEN amount < 0 THEN amount ELSE 0 END) AS total_expenses
FROM
    transaction
GROUP BY
    week, user_id, category
WITH NO DATA;
