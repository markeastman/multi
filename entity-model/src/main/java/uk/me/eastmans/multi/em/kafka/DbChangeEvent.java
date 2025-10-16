package uk.me.eastmans.multi.em.kafka;

/*
 * The entityName relates to what entity was affected
 * the operation is one of:
 *     'c' New record created
 *     'u' An existing record updated
 *     'd' An existing record deleted
 * the ids is an array of Long values each being a key value in the potentially composite key.
 * Generally the ids length will be one value.
 */
public record DbChangeEvent (String entityName, String operation, Long[] ids ) {}