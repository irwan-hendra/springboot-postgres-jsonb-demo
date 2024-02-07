# springboot-postgres-jsonb-demo

Demo of how we can avoid deserializing / serializing PostgreSQL's jsonb data type to java entity class if we just need to send it AS IS to Kafka Topic

### Benefits:
There are multiple benefits of NOT serializing / deserializing the jsonb data type when it's not needed:
- significantly reduce effort (to the tune of 3 weeks man-hours)
- increased reuse, the code can be used in any kind of jsonb data type
- simplify and reduce duplicate codes
