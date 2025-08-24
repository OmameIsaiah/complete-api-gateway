CREATE TABLE IF NOT EXISTS "users_table" (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  date_created TIMESTAMP DEFAULT NULL,
  last_modified TIMESTAMP DEFAULT NULL,
  uuid VARCHAR(255) DEFAULT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  is_online BOOLEAN DEFAULT NULL,
  last_login TIMESTAMP DEFAULT NULL,
  name VARCHAR(100) DEFAULT NULL,
  otp_code VARCHAR(255) DEFAULT NULL,
  otp_expire_time VARCHAR(255) DEFAULT NULL,
  password VARCHAR(255) DEFAULT NULL,
  user_token VARCHAR(255) DEFAULT NULL,
  user_type VARCHAR(255) DEFAULT NULL,
  verified BOOLEAN DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS "roles" (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  date_created TIMESTAMP DEFAULT NULL,
  last_modified TIMESTAMP DEFAULT NULL,
  uuid VARCHAR(255) DEFAULT NULL,
  description VARCHAR(255) DEFAULT NULL,
  name VARCHAR(255) DEFAULT NULL UNIQUE,
  permissions TEXT DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS "user_role" (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  date_created TIMESTAMP DEFAULT NULL,
  last_modified TIMESTAMP DEFAULT NULL,
  uuid VARCHAR(255) DEFAULT NULL,
  roleid BIGINT,
  userrole BIGINT,
  CONSTRAINT "FKss07htsrasc17qsq2o9422nyh" FOREIGN KEY (roleid) REFERENCES "roles"(id),
  CONSTRAINT "FK9vb9b0eakvmg912uiyvfg5on8" FOREIGN KEY (userrole) REFERENCES "users_table"(id)
);


MERGE INTO "roles" (id, date_created, last_modified, uuid, description, name, permissions)
VALUES (1, '2024-06-5 10:30:10', '2024-06-5 10:30:10', '18b56dc6-9813-440a-8c82-baf2eb10f264', 'Role for administrators', 'ROLE_ADMIN', '["CAN_VIEW_USERS","CAN_DELETE_USERS","CAN_ADD_ROLE","CAN_UPDATE_ROLE","CAN_DELETE_ROLE"]');

MERGE INTO "roles" (id, date_created, last_modified, uuid, description, name, permissions)
VALUES (2, '2024-06-5 10:30:10', '2024-06-5 10:30:10', 'cae15553-1148-49c1-8e9d-f3dea70fb4d1', 'Role for users', 'ROLE_USER', '["CAN_VIEW_USERS"]');
