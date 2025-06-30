package player;

import scotlandyard.*;

public class TargetAndTicket
{
    private Integer target = null;
    private Ticket ticket = null;

    // Initialises variables in the constructor.
    public TargetAndTicket(Integer target, Ticket ticket)
    {
        this.target = target;
        this.ticket = ticket;
    }

    // Returns the target location.
    public Integer getTarget()
    {
        return target;
    }

    // Returns the ticket type.
    public Ticket getTicket()
    {
        return ticket;
    }
}


