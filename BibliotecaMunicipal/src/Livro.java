public class Livro {

    private int id;
    private String isbn;
    private String titulo;
    private int stock;
    private int anoPublicacao;
    private String categoria;
    private String idioma;
    private String editora;
    private String genero;

    public Livro(int id, String isbn, String titulo, int stock, int anoPublicacao, String categoria, String idioma, String editora, String genero) {
        this.id = id;
        this.isbn = isbn;
        this.titulo = titulo;
        this.stock = stock;
        this.anoPublicacao = anoPublicacao;
        this.categoria = categoria;
        this.idioma = idioma;
        this.editora = editora;
        this.genero = genero;
    }

    public int getId() { return id; }
    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public int getStock() { return stock; }
    public int getAnoPublicacao() { return anoPublicacao; }
    public String getCategoria() { return categoria; }
    public String getIdioma() { return idioma; }
    public String getEditora() { return editora; }
    public String getGenero() { return genero; }

    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setStock(int stock) { this.stock  = stock;  }

    @Override
    public String toString() {
        return String.format("[%d] %-40s | ISBN: %-15s | Stock: %d | Ano: %d | %s | %s | %s", this.id, this.titulo, this.isbn, this.stock, this.anoPublicacao, this.categoria, this.genero, this.idioma);
    }
}