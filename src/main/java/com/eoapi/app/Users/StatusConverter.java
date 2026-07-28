package com.eoapi.app.Users;

public class StatusConverter implements jakarta.persistence.AttributeConverter<Status, String> {

	@Override
	public String convertToDatabaseColumn(Status status) {
		return status == null ? null : status.name();
	}

	@Override
	public Status convertToEntityAttribute(String dbValue) {
		return dbValue == null ? null : Status.valueOf(dbValue.toUpperCase());
	}

}
