import java.time.LocalDate;

public class Reserva {

    private int id;
    private LocalDate dataSaida;
    private LocalDate dataDevPrevista;
    private LocalDate dataDevReal;
    private String nomeRequisitante;
    private String nomeFuncionario;
    private int idExemplar;
    private String tituloLivro;

    public Reserva(int id, LocalDate dataSaida, LocalDate dataDevPrevista, LocalDate dataDevReal, String nomeRequisitante, String nomeFuncionario, int idExemplar, String tituloLivro) {
        this.id = id;
        this.dataSaida = dataSaida;
        this.dataDevPrevista = dataDevPrevista;
        this.dataDevReal = dataDevReal;
        this.nomeRequisitante = nomeRequisitante;
        this.nomeFuncionario = nomeFuncionario;
        this.idExemplar = idExemplar;
        this.tituloLivro = tituloLivro;
    }

    public int getId() { return id; }
    public LocalDate getDataSaida() { return dataSaida; }
    public LocalDate getDataDevPrevista() { return dataDevPrevista; }
    public LocalDate getDataDevReal() { return dataDevReal; }
    public String getNomeRequisitante() { return nomeRequisitante; }
    public String getNomeFuncionario() { return nomeFuncionario; }
    public int getIdExemplar() { return idExemplar; }
    public String getTituloLivro() { return tituloLivro; }

    public boolean estaDevolvido() {
        return dataDevReal != null;
    }

    public boolean estaAtrasado() {
        if (estaDevolvido()) {
            return false;
        } else {
            if (LocalDate.now().isAfter(dataDevPrevista)) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public String toString() {
        String devReal;
        if (estaDevolvido()) {
            devReal = dataDevReal.toString();
        } else {
            devReal = "Por devolver";
        }
        String alerta;
        if (estaAtrasado()) {
            alerta = "ATRASADO";
        } else {
            alerta = "";
        }
        return String.format("[%d] %s | Req: %s | Saida: %s | Prev: %s | Dev: %s", this.id, this.tituloLivro, this.nomeRequisitante, this.dataSaida, this.dataDevPrevista, devReal, alerta);
    }
}