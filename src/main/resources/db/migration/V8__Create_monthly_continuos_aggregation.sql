SELECT add_continuous_aggregate_policy('transaction_monthly_summary',
                                       start_offset => INTERVAL '3 month',
                                       end_offset   => INTERVAL '1 day',
                                       schedule_interval => INTERVAL '1 hour');