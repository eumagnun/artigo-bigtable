import com.google.api.gax.rpc.NotFoundException;
import com.google.api.gax.rpc.ServerStream;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminSettings;
import com.google.cloud.bigtable.admin.v2.models.*;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


public class Demo {
    private BigtableTableAdminClient adminClient = null;
    private BigtableDataClient dataClient = null;
    private List<Ativo> histData = new ArrayList<>();
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

    private void readFile() {

        try (BufferedReader br = new BufferedReader(new FileReader(Constantes.ARQUIVO_DADOS))) {
            String line;

            //skip first file first line
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                Date date=new SimpleDateFormat("dd/MM/yyyy").parse(values[1]);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

                histData.add(new Ativo(values[0],dateFormat.format(date), values[2], values[3], values[4], values[5], values[6], values[7]));
            }
            System.out.println(histData);
        } catch (IOException | ParseException e) {
            System.out.println("Error readFile");
            e.printStackTrace();
        }
    }

    public  void writeBatch() {


        try {
            BulkMutation bulkMutation = BulkMutation.create(Constantes.BT_TABLE_ID);

            for (Ativo ativo : this.histData) {

                bulkMutation.add(
                        ativo.getAtivo().concat("#").concat(ativo.getData()),
                        Mutation.create()
                                .setCell(
                                        "familiaA",
                                        "ativo",
                                        ativo.getAtivo())
                                .setCell(
                                        "familiaA",
                                        "data",
                                        ativo.getData())
                                .setCell(
                                        "familiaB",
                                        "alta",
                                        ativo.getAlta())
                                .setCell(
                                        "familiaB",
                                        "baixa",
                                        ativo.getBaixa())
                                .setCell(
                                        "familiaB",
                                        "abertura",
                                        ativo.getAbertura())
                                .setCell(
                                        "familiaB",
                                        "fechamento",
                                        ativo.getFechamento())
                                .setCell(
                                        "familiaC",
                                        "volume",
                                        ativo.getVolume())
                                .setCell(
                                        "familiaC",
                                        "fechamentoAjustado",
                                        ativo.getFechamentoAjustado())
                );
            }

            this.dataClient.bulkMutateRows(bulkMutation);

        } catch (Exception e) {
            System.out.println("Error writeBatch");
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Demo demo = new Demo();

        //demo.getTableMetaData();
        //demo.getRecordsByKey("MGLU3#20200101");
        //demo.getRecordsByKeyRange("MGLU3#20190101","MGLU3#20300101");

        demo.readFile();
        demo.writeBatch();
    }

}
