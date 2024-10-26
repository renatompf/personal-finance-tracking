SELECT add_continuous_aggregate_policy('transaction_weekly_summary',
                                       start_offset => INTERVAL '3 weeks',
                                       end_offset   => INTERVAL '1 day',
                                       schedule_interval => INTERVAL '1 hour');
