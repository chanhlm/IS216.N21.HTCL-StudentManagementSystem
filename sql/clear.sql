-- Drop the tables in reverse order to respect foreign key constraints

-- Drop the deadline_student table
DROP TABLE deadline_student CASCADE CONSTRAINTS;

-- Drop the student_time_off table
DROP TABLE student_time_off CASCADE CONSTRAINTS;

-- Drop the annoucement table
DROP TABLE annoucement CASCADE CONSTRAINTS;

-- Drop the grade_detail table
DROP TABLE grade_detail CASCADE CONSTRAINTS;

-- Drop the deadline table
DROP TABLE deadline CASCADE CONSTRAINTS;

-- Drop the course table
DROP TABLE course CASCADE CONSTRAINTS;

-- Drop the class table
DROP TABLE class CASCADE CONSTRAINTS;

-- Drop the users table
DROP TABLE users CASCADE CONSTRAINTS;

-- Drop the school table
DROP TABLE school CASCADE CONSTRAINTS;

-- Drop the account_type table
DROP TABLE account_type CASCADE CONSTRAINTS;

DROP TABLE captcha CASCADE CONSTRAINTS;

DROP TABLE user_password_reset CASCADE CONSTRAINTS;
