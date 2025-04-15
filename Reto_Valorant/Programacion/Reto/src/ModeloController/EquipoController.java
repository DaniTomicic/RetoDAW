

package ModeloController;

import Modelo.Equipo;
import Modelo.Juego;
import ModeloDAO.EquipoDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EquipoController {

    private final EquipoDAO eDAO;
    private LocalDate FECHAFUNDACION;
    //fecha fundacion hay que meterla en Juego
    private List<Equipo> equipos;

    public EquipoController(EquipoDAO eDAO) {
        this.eDAO = eDAO;
    }

    public void definirFechaFundacion(Juego j) {
        FECHAFUNDACION = j.getFechaSalida();
    }
    public List<Equipo> getEquipos() throws SQLException {
        equipos = eDAO.getEquipos();
        return equipos;
    }


    /**Metodos de validacion:
     * Pasa por este metodo validando el nombre y devolviendo el String a validarEquipo()*/
    public Equipo validarEquipo(String nombre) throws Exception {
        return eDAO.validarEquipo(nombre);
    }
    public String validarNombre(String nombre) throws Exception {
        Pattern p = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9 _-]{3,15}$"); //15 como mucho como en MER/MR
        Matcher matcher = p.matcher(nombre);
        if (!matcher.matches()) {
            throw new Exception("El nombre del equipo no es valido, el patron es de 3 a 15 carac. con espacios y guiones permitidos");
        }
        return nombre;
    }
    /**Se pasa a java.sql.Date en DAO*/
    public String validarFecha(String fecha) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            LocalDate fechaLocalDate = LocalDate.parse(fecha.trim(), formatter);
            if (fechaLocalDate.isAfter(LocalDate.now())) {
                throw new Exception("La fecha de fundacion no puede ser de anterior a la del juego");
            }
            return String.valueOf(fechaLocalDate);
        }catch (DateTimeParseException e){
            throw new DateTimeParseException("La fecha no sigue un formato valido (dd-mm-aaaa)", fecha,0);
        }
    }

    /**CRUD de EquipoController*/
    public boolean borrarEquipo(String nombreEquipo) throws Exception {
        return eDAO.borrarEquipo(nombreEquipo);
    }
    public boolean crearEquipo(String nombre, String fechaFundacion) throws Exception {
        return eDAO.crearEquipo(validarNombre(nombre),validarFecha(fechaFundacion));
    }
    public boolean actualizarEquipoFecha(String nombre, String fechaFundacion) throws Exception {
        return eDAO.actualizarFechaEquipo(validarFecha(nombre),validarFecha(fechaFundacion));
    }

}
