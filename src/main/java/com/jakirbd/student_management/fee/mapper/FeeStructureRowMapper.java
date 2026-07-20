package com.jakirbd.student_management.fee.mapper;

import com.jakirbd.student_management.fee.dto.response.FeeStructureResponse;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class FeeStructureRowMapper
        implements RowMapper<FeeStructureResponse> {

    @Override
    public FeeStructureResponse mapRow(
            ResultSet resultSet,
            int rowNum
    ) throws SQLException {

        FeeStructureResponse response =
                new FeeStructureResponse();

        response.setFeeStructureId(
                resultSet.getObject(
                        "FEE_STRUCTURE_ID",
                        Long.class
                )
        );

        response.setAcademicYearId(
                resultSet.getObject(
                        "ACADEMIC_YEAR_ID",
                        Long.class
                )
        );

        response.setAcademicYearClassId(
                resultSet.getObject(
                        "ACADEMIC_YEAR_CLASS_ID",
                        Long.class
                )
        );

        response.setSectionId(
                resultSet.getObject("SECTION_ID", Long.class)
        );

        response.setFeeTypeId(
                resultSet.getObject("FEE_TYPE_ID", Long.class)
        );

        response.setStructureName(
                resultSet.getString("STRUCTURE_NAME")
        );

        response.setAmount(
                resultSet.getBigDecimal("AMOUNT")
        );

        response.setFrequency(
                resultSet.getString("FREQUENCY")
        );

        response.setDueDay(
                resultSet.getObject("DUE_DAY", Integer.class)
        );

        Date effectiveFrom =
                resultSet.getDate("EFFECTIVE_FROM");

        if (effectiveFrom != null) {
            response.setEffectiveFrom(
                    effectiveFrom.toLocalDate()
            );
        }

        Date effectiveTo =
                resultSet.getDate("EFFECTIVE_TO");

        if (effectiveTo != null) {
            response.setEffectiveTo(
                    effectiveTo.toLocalDate()
            );
        }

        response.setFineType(
                resultSet.getString("FINE_TYPE")
        );

        response.setFineValue(
                resultSet.getBigDecimal("FINE_VALUE")
        );

        response.setDescription(
                resultSet.getString("DESCRIPTION")
        );

        response.setStatus(
                resultSet.getString("STATUS")
        );

        Timestamp createdAt =
                resultSet.getTimestamp("CREATED_AT");

        if (createdAt != null) {
            response.setCreatedAt(
                    createdAt.toLocalDateTime()
            );
        }

        Timestamp updatedAt =
                resultSet.getTimestamp("UPDATED_AT");

        if (updatedAt != null) {
            response.setUpdatedAt(
                    updatedAt.toLocalDateTime()
            );
        }

        response.setCreatedBy(
                resultSet.getObject("CREATED_BY", Long.class)
        );

        response.setUpdatedBy(
                resultSet.getObject("UPDATED_BY", Long.class)
        );

        return response;
    }
}