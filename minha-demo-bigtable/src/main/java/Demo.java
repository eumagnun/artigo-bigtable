import com.google.api.gax.rpc.NotFoundException;
import com.google.api.gax.rpc.ServerStream;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminSettings;
import com.google.cloud.bigtable.admin.v2.models.*;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.*;

import java.io.IOException;
import java.util.Collection;


public class Demo {
    BigtableTableAdminClient adminClient = null;
    BigtableDataClient dataClient = null;

    public Demo() {
        createDataClient();
        createAdminClient();
    }

    private void createDataClient() {
        BigtableDataClient dataClient = null;
        try {
            this.dataClient = BigtableDataClient.create(Constantes.BT_PROJECT_ID, Constantes.BT_INSTANCE_ID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createAdminClient() {
        try {
            BigtableTableAdminSettings adminSettings =
                    BigtableTableAdminSettings.newBuilder()
                            .setProjectId(Constantes.BT_PROJECT_ID)
                            .setInstanceId(Constantes.BT_INSTANCE_ID)
                            .build();

            this.adminClient = BigtableTableAdminClient.create(adminSettings);
        } catch (IOException e) {
            System.out.println("Error createAdmin");
            e.printStackTrace();
        }
    }

    private  void getRecordsByKeyRange(String start, String end) {
        System.out.println("\ngetRecordsByKeyRange");
        try {
            Query query = Query.create(Constantes.BT_TABLE_ID).range(start, end);
            ServerStream<Row> rows = this.dataClient.readRows(query);

            rows.forEach(r->printRecord(r));

        } catch (Exception e) {
            System.out.println("Error getRecordsByKeyRange");
            e.printStackTrace();
        }
    }

    private void getRecordsByKey( String key) {
        System.out.println("\ngetRecordsByKey");
        try {

            Row row = this.dataClient.readRow(Constantes.BT_TABLE_ID, key);
            printRecord(row);

        } catch (Exception e) {
            System.out.println("Error getRecordsByKey");
            e.printStackTrace();
        }
    }

    public void getTableMetaData() {
        System.out.println("\nPrinting table metadata");
        try {
            Table table = this.adminClient.getTable(Constantes.BT_TABLE_ID);
            System.out.println("Table: " + table.getId());
            Collection<ColumnFamily> columnFamilies = table.getColumnFamilies();
            for (ColumnFamily columnFamily : columnFamilies) {
                System.out.printf(
                        "Column family: %s%nGC Rule: %s%n",
                        columnFamily.getId(), columnFamily.getGCRule().toString());
            }
        } catch (NotFoundException e) {
            System.out.println("Error getTableMeta");
            e.printStackTrace();
        }
    }

    private static void printRecord(Row row) {
        System.out.println(row.getKey().toStringUtf8());
        for (RowCell cell : row.getCells()) {
            System.out.println(cell.getQualifier().toStringUtf8()+" - "+ cell.getValue().toStringUtf8());
        }
        System.out.println("-----");
    }

    public static void main(String[] args) {
        Demo demo = new Demo();

        demo.getTableMetaData();
        demo.getRecordsByKey("MGLU3#20200101");
        demo.getRecordsByKeyRange("MGLU3#20190101","MGLU3#20300101");
    }

}
