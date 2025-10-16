package uk.me.eastmans.multi.em.kafka;

/*
 * The tableName relates to what table was affected
 * the operation is one of:
 *     'c' New record created
 *     'u' An existing record updated
 *     'd' An existing record deleted
 * the ids is an array of Long values each being a key value in the potentially composite key.
 * Generally the ids length will be one value.
 */
public record DbChangeEvent (String tableName, String operation, Long[] ids ) {}
