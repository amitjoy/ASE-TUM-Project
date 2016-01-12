package in.tum.de.ase.util;

import java.time.LocalDate;

import in.tum.de.ase.model.Eticket;
import in.tum.de.ase.model.EticketType;

public interface TicketParser {

	public static Eticket parse(final String value) {
		final int year = Integer.valueOf(value.substring(0, 4));
		final int month = Integer.valueOf(value.substring(5, 6));
		final int dayOfMonth = Integer.valueOf(value.substring(6, 8));
		final LocalDate date = LocalDate.of(year, month, dayOfMonth);
		final EticketType type = "1".equals(value.substring(9, 10)) ? EticketType.SINGLE : EticketType.SEASON;
		return new Eticket(value.substring(9), type, date);
	}

}
