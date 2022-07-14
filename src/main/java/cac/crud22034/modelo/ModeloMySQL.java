package cac.crud22034.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Charly Cimino Aprendé más Java en mi canal:
 * https://www.youtube.com/c/CharlyCimino Encontrá más código en mi repo de
 * GitHub: https://github.com/CharlyCimino
 */
public class ModeloMySQL implements Modelo {

    private static final String GET_ALL_QUERY = "SELECT * FROM alumnos";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM alumnos WHERE id = ?";
    private static final String ADD_QUERY = "INSERT INTO alumnos VALUES (null, ?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE alumnos SET nombre=?, apellido=?, fechaNacimiento = ?, mail=?, fotoBase64=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM alumnos WHERE id = ?";

    @Override
    public List<Alumno> getAlumnos() {
        List<Alumno> alumnos = new ArrayList<>();
        try ( Connection con = Conexion.getConnection();  PreparedStatement ps = con.prepareStatement(GET_ALL_QUERY);  ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                alumnos.add(rsToAlumno(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error de SQL", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error al leer alumnos", ex);
        }
        return alumnos;
    }

    @Override
    public Alumno getAlumno(int id) {
        Alumno alu = null;
        try ( Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(GET_BY_ID_QUERY);) {
            ps.setInt(1, id);
            try ( ResultSet rs = ps.executeQuery();) {
                rs.next();
                alu = rsToAlumno(rs);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error de SQL", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error al leer alumno con ID " + id, ex);
        }
        return alu;
    }

    @Override
    public int addAlumno(Alumno alumno) {
        int regsAgregados = 0;
        try ( Connection con = Conexion.getConnection();  PreparedStatement ps = con.prepareStatement(ADD_QUERY);) {
            fillPreparedStatement(ps, alumno);
            regsAgregados = ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error de SQL", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error al agregar alumno " + alumno.getNombreCompleto(), ex);
        }
        return regsAgregados;
    }

    @Override
    public int updateAlumno(Alumno alumno) {
        int regsActualizados = 0;
        try ( Connection con = Conexion.getConnection();  PreparedStatement ps = con.prepareStatement(UPDATE_QUERY);) {
            fillPreparedStatement(ps, alumno);
            ps.setInt(6, alumno.getId());
            regsActualizados = ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error de SQL", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error al editar alumno " + alumno.getNombreCompleto(), ex);
        }
        return regsActualizados;
    }

    @Override
    public int removeAlumno(int id) {
        int regsBorrados = 0;
        try ( Connection con = Conexion.getConnection();  PreparedStatement ps = con.prepareStatement(DELETE_QUERY);) {
            ps.setInt(1, id);
            regsBorrados = ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error de SQL", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error al borrar alumno con ID " + id, ex);
        }
        return regsBorrados;
    }

    private Alumno rsToAlumno(ResultSet rs) throws SQLException {
        int idAlu = rs.getInt("id");
        String nombre = rs.getString("nombre");
        String apellido = rs.getString("apellido");
        String mail = rs.getString("mail");
        String fechaNac = rs.getString("fechaNacimiento");
        String fotoBase64 = rs.getString("fotoBase64");
        return new Alumno(idAlu, nombre, apellido, mail, fechaNac, fotoBase64);
    }

    private void fillPreparedStatement(PreparedStatement ps, Alumno alumno) throws SQLException {
        ps.setString(1, alumno.getNombre());
        ps.setString(2, alumno.getApellido());
        ps.setString(3, alumno.getFechaNacimiento());
        ps.setString(4, alumno.getMail());
        ps.setString(5, alumno.getFoto());
    }

}
