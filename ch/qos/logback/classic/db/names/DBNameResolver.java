package ch.qos.logback.classic.db.names;

public interface DBNameResolver {
  <N extends Enum<?>> String getTableName(N paramN);
  
  <N extends Enum<?>> String getColumnName(N paramN);
}


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\qos\logback\classic\db\names\DBNameResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */