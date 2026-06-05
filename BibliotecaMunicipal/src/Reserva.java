import java.time.LocalDate;

public class Reserva {

    private int       id;
    private LocalDate dataSaida;
    private LocalDate dataDevPrevista;
    private LocalDate dataDevReal;
    private String    nomeRequisitante;
    private String    nomeFuncionario;
    private int       idExemplar;
    private String    tituloLivro;

    public Reserva(int id, LocalDate dataSaida, LocalDate dataDevPrevista,
                   LocalDate dataDevReal, String nomeRequisitante,
                   String nomeFuncionario, int idExemplar, String tituloLivro) {
        this.id               = id;
        this.dataSaida        = dataSaida;
        this.dataDevPrevista  = dataDevPrevista;
        this.dataDevReal      = dataDevReal;
        this.nomeRequisitante = nomeRequisitante;
        this.nomeFuncionario  = nomeFuncionario;
        this.idExemplar       = idExemplar;
        this.tituloLivro      = tituloLivro;
    }

    public int       getId()               { return id; }
    public LocalDate getDataSaida()        { return dataSaida; }
    public LocalDate getDataDevPrevista()  { return dataDevPrevista; }
    public LocalDate getDataDevReal()      { return dataDevReal; }
    public String    getNomeRequisitante() { return nomeRequisitante; }
    public String    getNomeFuncionario()  { return nomeFuncionario; }
    public int       getIdExemplar()       { return idExemplar; }
    public String    getTituloLivro()      { return tituloLivro; }

    public boolean estaDevolvido() {
        return dataDevReal != null;
    }

    public boolean estaAtrasado() {
        if (estaDevolvido()) return false;
        return LocalDate.now().isAfter(dataDevPrevista);
    }

    @Override
    public String toString() {
        String devReal = estaDevolvido() ? dataDevReal.toString() : "Por devolver";
        String alerta  = estaAtrasado()  ? " *** ATRASADO ***" : "";
        return String.format("[%d] %-35s | Req: %-20s | Saida: %s | Prev: %s | Dev: %s%s",
                id, tituloLivro, nomeRequisitante,
                dataSaida, dataDevPrevista, devReal, alerta);
    }
}