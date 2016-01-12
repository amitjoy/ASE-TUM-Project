package in.tum.de.ase.constants;

public enum Constants {

	ALREADY_VALIDATED_MSG("Provided Ticket is already validated"), DIALOG_BOX_HEADER(
			"Ticket Validation"), INVALID_TICKET_MSG("Invalid Ticket Provided"), VALIDATED_MSG("Ticket is Validated");

	private final String msg;

	Constants(final String msg) {
		this.msg = msg;
	}

	public String getValue() {
		return this.msg;
	}

}
