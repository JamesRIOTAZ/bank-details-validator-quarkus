package bankdetailsvalidator.quarkus.supplier;


import bankdetailsvalidator.quarkus.exception.TableSupplierRuntimeException;
import bankdetailsvalidator.quarkus.models.ModulusWeightTableEntry;


import io.quarkus.bootstrap.classloading.ClassPathResource;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Component
public class ModulusWeightTableSupplier implements Supplier<List<ModulusWeightTableEntry>>{

    private static final String MODULUS_WEIGHT_TABLE = "/AccountModulus_Weight_Table.txt";

    @Override
    public List<ModulusWeightTableEntry> get(){

        final List<ModulusWeightTableEntry> modulusWeightTable = new ArrayList<>();
        try(final BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(MODULUS_WEIGHT_TABLE)))){
            String line;
            while ((line = br.readLine()) != null) {
                final String[] fields = line.split("\\s+");
                modulusWeightTable.add(new ModulusWeightTableEntry(fields));
            }
        }
        catch (final IOException e) {
            System.out.println("error " + e.getMessage());
            throw new TableSupplierRuntimeException("An error occurred loading the modulus weight table or sort code substitution table", e);
        }
        return modulusWeightTable;
    }
}
