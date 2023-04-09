package com.pagonxt.onetradefinance.external.backend.api.model.cle;

import java.util.ArrayList;
import java.util.List;

/**
 * This class returns a list of export collection advances(dto)
 * @author -
 * @version jdk-11.0.13
 * @see ExportCollectionAdvanceDto
 * @since jdk-11.0.13
 */
public class ExportCollectionAdvanceList extends ArrayList<ExportCollectionAdvanceDto> {

    /**
     * Class constructor
     * @param exportCollections list of export collection advances(dto)
     */
    public ExportCollectionAdvanceList(List<ExportCollectionAdvanceDto> exportCollections) {
        super(exportCollections);
    }
}
