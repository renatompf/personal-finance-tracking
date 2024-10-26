CREATE MATERIALIZED VIEW transaction_monthly_summary
            WITH (timescaledb.continuous) AS
SELECT
    time_bucket('1 month', date) AS month,
    user_id,
    category,
    SUM(CASE WHEN amount > 0 THEN amount ELSE 0 END) AS total_income,
    SUM(CASE WHEN amount < 0 THEN amount ELSE 0 END) AS total_expenses
FROM
    transaction
GROUP BY
    month, user_id, category
WITH NO DATA;
