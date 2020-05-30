package bankdetailsvalidator.quarkus.supplier;


import bankdetailsvalidator.quarkus.exception.TableSupplierRuntimeException;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Component
public class SortCodeSubstitutionTableSupplier implements Supplier<Map<String, String>> {

    private static final String SORT_CODE_SUBSTITUTION_TABLE = "/SCSUBTAB.txt";

    @Override
    public Map<String, String> get() {

        final Map<String, String> sortCodeSubstitutionTable = new HashMap<>();

        try (final BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(SORT_CODE_SUBSTITUTION_TABLE)))) {
            String line;
            while ((line = br.readLine()) != null) {
                final String[] fields = line.split("\\s+");
                sortCodeSubstitutionTable.put(fields[0], fields[1]);
            }
        } catch (final IOException e) {
            throw new TableSupplierRuntimeException("An error occurred loading the sort code substitution table", e);
        }
        return sortCodeSubstitutionTable;
    }
}