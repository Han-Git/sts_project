package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.sqlservice.SqlService;

public class UserDaoJdbc implements UserDao{
	private JdbcTemplate jdbcTemplate;	//#p260
	private DataSource dataSource;
	private SqlService sqlService;
	
	public void setSqlService(SqlService sqlService) {
		this.sqlService = sqlService;
	}
	
	private RowMapper<User> userMapper = new RowMapper<User>(){
											public User mapRow(ResultSet rs, int rowNum)throws SQLException{
												User user = new User();
												user.setId(rs.getString("id"));
												user.setName(rs.getString("name"));
												user.setPassword(rs.getString("password"));
												user.setLevel(Level.valueOf(rs.getInt("level")));
												user.setLogin(rs.getInt("login"));
												user.setRecommend(rs.getInt("recommend"));
												user.setEmail(rs.getString("email"));
												return user;
											}
										};
	public void setDataSource(DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);	//#p260
		this.dataSource = dataSource;
	}

	public User get(String id){	//#p274
		return this.jdbcTemplate.queryForObject( this.sqlService.getSql("userGet")
					, new Object[] {id}
					, this.userMapper	//#p273
				);
	}

	public List<User> getAll(){
		return this.jdbcTemplate.query(this.sqlService.getSql("userGetAll"),
				this.userMapper	//#p273
		);
	}
	
	public void add(final User user){	// p274
		this.jdbcTemplate.update(this.sqlService.getSql("userAdd"),
				user.getId(),user.getName(),user.getPassword(),user.getEmail(),
				user.getLevel().intValue(),user.getLogin(),	user.getRecommend());
	}
	
	public void deleteAll(){
		this.jdbcTemplate.update(this.sqlService.getSql("userDeleteAll"));	// #p261
	}
	
	public int getCount(){	//#p274
		return this.jdbcTemplate.queryForInt(this.sqlService.getSql("userGetCount"));	//#p264
	}
	
	public void update(User user) {
		this.jdbcTemplate.update(this.sqlService.getSql("userUpdate"),
				user.getName(),user.getPassword(),
				user.getLevel().intValue(), user.getLogin(),user.getRecommend(),user.getEmail(),
				user.getId()
				);
	}
	
}
