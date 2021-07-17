package protobuftutorial;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import protobuftutorial.FilmeProto.MFilme;
import protobuftutorial.FilmeProto.MListaFilmes;

public class Filme {

	private int filmeId;
	private String titulo;
	private int ano;
	private int avaliacaoImdb;
	URL linkParaTrailerNoYoutube;
	List<Filme> tresFilmesMaisRelacionados = new ArrayList<Filme>();

	public Filme(int filmeId, String titulo, int ano, int avaliacaoImdb, String linkTrailer)
			throws MalformedURLException {
		super();
		this.filmeId = filmeId;
		this.titulo = titulo;
		this.ano = ano;
		this.avaliacaoImdb = avaliacaoImdb;
		this.linkParaTrailerNoYoutube = new URL(linkTrailer);
	}

	public void addFilmeRelacionado(Filme filmeRelacionado) {
		if (tresFilmesMaisRelacionados.size() == 3) {
			tresFilmesMaisRelacionados.remove(0);
		}
		tresFilmesMaisRelacionados.add(filmeRelacionado);
	}

	@Override
	public String toString() {
		return "Filme [filmeId=" + filmeId + ", titulo=" + titulo + ", ano=" + ano + ", avaliacaoImdb=" + avaliacaoImdb
				+ ", linkParaTrailerNoYoutube=" + linkParaTrailerNoYoutube + ", tresFilmesMaisRelacionados="
				+ tresFilmesMaisRelacionados + "]";
	}

	private static MFilme constroiMfilme(Filme filme) {
		protobuftutorial.FilmeProto.MFilme.Builder mFilmeBuilder = protobuftutorial.FilmeProto.MFilme.newBuilder();

		mFilmeBuilder.setId(filme.filmeId);
		mFilmeBuilder.setTitulo(filme.titulo);
		mFilmeBuilder.setAno(filme.ano);
		mFilmeBuilder.setAvaliacaoImdb(filme.avaliacaoImdb);
		mFilmeBuilder.setLinkTrailer(filme.linkParaTrailerNoYoutube.toString());

		List<MFilme> listaMFilme = new ArrayList<FilmeProto.MFilme>();
		for (Filme filmeRelacionado : filme.tresFilmesMaisRelacionados) {
			listaMFilme.add(constroiMfilme(filmeRelacionado));
		}
		mFilmeBuilder.addAllFilmesRelacionados(listaMFilme);

		return mFilmeBuilder.build();
	}

	private static MListaFilmes constroiMListaFilmes(List<Filme> listFilmes) {
		List<MFilme> listaMFilme = new ArrayList<FilmeProto.MFilme>();
		for (Filme filme : listFilmes) {
			listaMFilme.add(constroiMfilme(filme));
		}

		protobuftutorial.FilmeProto.MListaFilmes.Builder mListaFilmesBuilder = protobuftutorial.FilmeProto.MListaFilmes
				.newBuilder();

		mListaFilmesBuilder.addAllFilmes(listaMFilme);

		return mListaFilmesBuilder.build();
	}

	private static List<Filme> geralistaFilmes() {
		List<Filme> listFilmes = new ArrayList<Filme>();
		Random random = new Random();

		for (int i = 0; i < 10; i++) {
			try {
				int numi = i + 1;
				Filme filme = new Filme(numi, "Filme " + numi, 1985 + numi, Math.round(random.nextFloat() * 5f),
						"https://www.youtube.com/filme" + numi);

				for (int j = 0; j < 3; j++) {
					int numj = 10 * (i + 1) + j;
					Filme filmeRelacionado = new Filme(numj, "Filme " + numj, 1985 + numj,
							Math.round(random.nextFloat() * 5f), "https://www.youtube.com/filme" + numj);

					filme.addFilmeRelacionado(filmeRelacionado);
				}

				listFilmes.add(filme);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		return listFilmes;
	}

	private static void escreveArquivoFilmes(String pathFile, MListaFilmes mListaFilmes) throws IOException {
		FileOutputStream outputStream = new FileOutputStream(pathFile);
		mListaFilmes.writeTo(outputStream);
		outputStream.close();
	}

	private static void leArquivoFilmes(String pathFile) throws IOException {
		FileInputStream inputStream = new FileInputStream(pathFile);
		MListaFilmes mListaFilmes = MListaFilmes.parseFrom(inputStream);

		List<MFilme> listaMFilmes = mListaFilmes.getFilmesList();
		for (MFilme mFilme : listaMFilmes) {
			System.out.println(mFilme);
		}

		inputStream.close();
	}

	public static void main(String[] args) {
		String pathFile = "c:\\java\\temp\\filmes.data";

		List<Filme> listaFilmes = geralistaFilmes();
		MListaFilmes mListaFilmes = constroiMListaFilmes(listaFilmes);
		try {
			escreveArquivoFilmes(pathFile, mListaFilmes);
			leArquivoFilmes(pathFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
