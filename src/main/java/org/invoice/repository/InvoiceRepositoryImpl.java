package org.invoice.repository;

import org.invoice.domain.Invoice;
import org.invoice.domain.Student;
import org.invoice.exception.RepositoryException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InvoiceRepositoryImpl implements InvoiceRepository {
    private final Connection connection;
    public InvoiceRepositoryImpl() {
        try { connection = ConnectionManager.getConnection(); }
        catch(SQLException e){ throw new RepositoryException("DB error", e); }
    }

    public Invoice save(Invoice i) {
        if(i.getId()==null){
            String sql="INSERT INTO invoices (invoice_number, student_id, subtotal, discount, tax_rate, tax_amount, total, date_issued, due_date) VALUES(?,?,?,?,?,?,?,?,?)";
            try(PreparedStatement ps=connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                ps.setString(1, i.getInvoiceNumber());
                ps.setObject(2, (i.getStudent()==null?null:i.getStudent().getId()), Types.BIGINT);
                ps.setDouble(3, i.getSubtotal());
                ps.setDouble(4, i.getDiscount());
                ps.setDouble(5, i.getTaxRate());
                ps.setDouble(6, i.getTaxAmount());
                ps.setDouble(7, i.getTotal());
                ps.setDate(8, Date.valueOf(i.getDateIssued()));
                ps.setDate(9, Date.valueOf(i.getDueDate()));
                ps.executeUpdate();
                try(ResultSet rs=ps.getGeneratedKeys()){
                    if(rs.next()) i.setId(rs.getLong(1));
                }
            }catch(Exception e){ throw new RepositoryException("Save invoice failed", e); }
        } else {
            String sql="UPDATE invoices SET invoice_number=?, subtotal=?, discount=?, tax_rate=?, tax_amount=?, total=?, date_issued=?, due_date=? WHERE id=?";
            try(PreparedStatement ps=connection.prepareStatement(sql)){
                ps.setString(1, i.getInvoiceNumber());
                ps.setDouble(2, i.getSubtotal());
                ps.setDouble(3, i.getDiscount());
                ps.setDouble(4, i.getTaxRate());
                ps.setDouble(5, i.getTaxAmount());
                ps.setDouble(6, i.getTotal());
                ps.setDate(7, Date.valueOf(i.getDateIssued()));
                ps.setDate(8, Date.valueOf(i.getDueDate()));
                ps.setLong(9, i.getId());
                ps.executeUpdate();
            }catch(Exception e){ throw new RepositoryException("Update invoice failed", e); }
        }
        return i;
    }

    public Invoice findById(Long id) {
        String sql="SELECT i.id,i.invoice_number,i.subtotal,i.discount,i.tax_rate,i.tax_amount,i.total,i.date_issued,i.due_date,s.id as sid,s.name as sname,s.email as semail " +
                "FROM invoices i LEFT JOIN students s ON i.student_id=s.id WHERE i.id=?";
        try(PreparedStatement ps=connection.prepareStatement(sql)){
            ps.setLong(1,id);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next()){
                    Invoice inv=new Invoice();
                    inv.setId(rs.getLong("id"));
                    inv.setInvoiceNumber(rs.getString("invoice_number"));
                    inv.setSubtotal(rs.getDouble("subtotal"));
                    inv.setDiscount(rs.getDouble("discount"));
                    inv.setTaxRate(rs.getDouble("tax_rate"));
                    inv.setTaxAmount(rs.getDouble("tax_amount"));
                    inv.setTotal(rs.getDouble("total"));
                    inv.setDateIssued(rs.getDate("date_issued").toLocalDate());
                    inv.setDueDate(rs.getDate("due_date").toLocalDate());
                    Long sid=rs.getLong("sid");
                    if(!rs.wasNull()){
                        Student st=new Student();
                        st.setId(sid);
                        st.setName(rs.getString("sname"));
                        st.setEmail(rs.getString("semail"));
                        inv.setStudent(st);
                    }
                    return inv;
                }
            }
        }catch(Exception e){ throw new RepositoryException("Find invoice by id failed", e); }
        return null;
    }

    public List<Invoice> findAll() {
        List<Invoice> list=new ArrayList<>();
        String sql="SELECT i.id,i.invoice_number,i.subtotal,i.discount,i.tax_rate,i.tax_amount,i.total,i.date_issued,i.due_date,s.id as sid,s.name as sname,s.email as semail " +
                "FROM invoices i LEFT JOIN students s ON i.student_id=s.id";
        try(Statement Student = connection.createStatement(); ResultSet rs=Student.executeQuery(sql)){
            while(rs.next()){
                Invoice inv=new Invoice();
                inv.setId(rs.getLong("id"));
                inv.setInvoiceNumber(rs.getString("invoice_number"));
                inv.setSubtotal(rs.getDouble("subtotal"));
                inv.setDiscount(rs.getDouble("discount"));
                inv.setTaxRate(rs.getDouble("tax_rate"));
                inv.setTaxAmount(rs.getDouble("tax_amount"));
                inv.setTotal(rs.getDouble("total"));
                inv.setDateIssued(rs.getDate("date_issued").toLocalDate());
                inv.setDueDate(rs.getDate("due_date").toLocalDate());
                Long sid=rs.getLong("sid");
                if(!rs.wasNull()){
                    Student student =new Student();
                    student.setId(sid);
                    student.setName(rs.getString("sname"));
                    student.setEmail(rs.getString("semail"));
                    inv.setStudent(student);
                }
                list.add(inv);
            }
        }catch(Exception e){ throw new RepositoryException("List invoices failed", e); }
        return list;
    }

    public List<Invoice> findByCustomerName(String name) {
        List<Invoice> list=new ArrayList<>();
        String sql="SELECT i.id,i.invoice_number,i.subtotal,i.discount,i.tax_rate,i.tax_amount,i.total,i.date_issued,i.due_date,s.id as sid,s.name as sname,s.email as semail " +
                "FROM invoices i LEFT JOIN students s ON i.student_id=s.id WHERE s.name LIKE ?";
        try(PreparedStatement ps=connection.prepareStatement(sql)){
            ps.setString(1, "%"+name+"%");
            try(ResultSet rs=ps.executeQuery()){
                while(rs.next()){
                    Invoice inv=new Invoice();
                    inv.setId(rs.getLong("id"));
                    inv.setInvoiceNumber(rs.getString("invoice_number"));
                    inv.setSubtotal(rs.getDouble("subtotal"));
                    inv.setDiscount(rs.getDouble("discount"));
                    inv.setTaxRate(rs.getDouble("tax_rate"));
                    inv.setTaxAmount(rs.getDouble("tax_amount"));
                    inv.setTotal(rs.getDouble("total"));
                    inv.setDateIssued(rs.getDate("date_issued").toLocalDate());
                    inv.setDueDate(rs.getDate("due_date").toLocalDate());
                    Long sid=rs.getLong("sid");
                    if(!rs.wasNull()){
                        Student st=new Student();
                        st.setId(sid);
                        st.setName(rs.getString("sname"));
                        st.setEmail(rs.getString("semail"));
                        inv.setStudent(st);
                    }
                    list.add(inv);
                }
            }
        }catch(Exception e){ throw new RepositoryException("Search invoice by name failed", e); }
        return list;
    }

    public boolean delete(Long id) {
        String sql="DELETE FROM invoices WHERE id=?";
        try(PreparedStatement ps=connection.prepareStatement(sql)){
            ps.setLong(1,id);
            int rows=ps.executeUpdate();
            return rows>0;
        }catch(Exception e){ throw new RepositoryException("Delete invoice failed", e); }
    }
}
