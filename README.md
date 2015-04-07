# Dropwizard MyBatis Support

[![Build Status](https://circleci.com/gh/login-box/dropwizard-mybatis.svg)](https://circleci.com/gh/login-box/dropwizard-mybatis)

## Defining Mapper Interfaces

A mapper interface defines methods that map to SQL queries or procedure calls.
For example, an interface for managing users might look like

```java
package com.example.helloworld.mappers;

public interface UsersMapper {
    User findByUsername(@Param("username") String username);

    void addUser(@Param("user") User user);
}
```

MyBatis binds these interfaces to actual SQL stored in a mapper config file in
the same package. In this case, `UsersMapper.xml` would look like this:

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.helloworld.mappers.UsersMapper">
    <select id="findByUsername" resultType="User">
    <![CDATA[
        select username, email
        from users
        where username = #{username}
    ]]>
    </select>

    <resultMap id="User" type="com.example.helloworld.mappers.User">
        <id column="username" property="username" />
        <result column="email" property="email" />
    </resultMap>

    <insert id="addUser">
    <![CDATA[
        insert into users (username, email)
        values (#{user.username}, #{user.email})
    ]]>
    </insert>
</mapper>
```

See the [MyBatis
documentation](http://mybatis.github.io/mybatis-3/sqlmap-xml.html) for more
thorough examples, covering things like nested results and complex relational
mapping for joins.

## Configuring The Database

Before you can use MyBatis in your Dropwizard app, you'll need to configure a
database connection pool in your application configuration class:

```java
package com.example.helloworld;

/* imports */

public class HelloWorldConfiguration extends Configuration {
    @Valid
    @NotNull
    private DataSourceFactory dataSourceFactory = new DataSourceFactory();

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return this.dataSourceFactory;
    }
}
```

You can configure the database connection in your config file:

```yaml
database:
    # the name of your JDBC driver
    driverClass: org.postgresql.Driver

    # the username
    user: pg-user

    # the password
    password: iAMs00perSecrEET

    # the JDBC URL
    url: jdbc:postgresql://db.example.com/db-prod

    # any properties specific to your JDBC driver:
    properties:
        charSet: UTF-8

    # the maximum amount of time to wait on an empty pool before throwing an exception
    maxWaitForConnection: 1s

    # the SQL query to run when validating a connection's liveness
    validationQuery: "/* MyService Health Check */ SELECT 1"

    # the timeout before a connection validation queries fail
    validationQueryTimeout: 3s

    # the minimum number of connections to keep open
    minSize: 8

    # the maximum number of connections to keep open
    maxSize: 32

    # whether or not idle connections should be validated
    checkConnectionWhileIdle: false

    # the amount of time to sleep between runs of the idle connection validation, abandoned cleaner and idle pool resizing
    evictionInterval: 10s

    # the minimum amount of time an connection must sit idle in the pool before it is eligible for eviction
    minIdleTime: 1 minute
```

## Adding MyBatis To Your App

Once your database connection is configured, you can register a `MybatisBundle`
during your application's bootstrap. I strongly recommend storing the bundle in
a `private final` attribute on your application class, for later use:

```java
package com.example.helloworld;

public class HelloWorld extends Application<HelloWorldConfiguration> {

    public static void main(String... args) throws Exception {
        new HelloWorld().run(args);
    }

    private final MybatisBundle<HelloWorldConfiguration> mybatisBundle
            = new MybatisBundle<HelloWorldConfiguration>("com.example.helloworld") {
        @Override
        public DataSourceFactory getDataSourceFactory(HelloWorldConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    @Override
    public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {
        bootstrap.addBundle(mybatisBundle);
    }
    // . . .
}
```

You can use MyBatis from your resource classes by calling
`getSqlSessionFactory()` on the bundle to get its session factory:

```java
    public void run(HelloWorldConfiguration configuration, Environment environment) throws Exception {
        SqlSessionFactory sessionFactory = mybatisBundle.getSqlSessionFactory();
        environment.jersey().register(new ExampleResource(sessionFactory));
        // . . .
    }
```

The `SqlSessionFactory` will only be available after `initialize` completes.

## Making Database Calls

Your resources can use the `SqlSessionFactory`'s `openSession()` methods to
begin database conversations:

```
package com.example.helloworld.resources;

@Path("/user/{username}")
public class ExampleResource {
    private final SqlSessionFactory sessionFactory;

    public ExampleResource(SqlSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @GET
    public User getUser(@PathParam("username") String username) {
        try (SqlSession session = sessionFactory.openSession()) {
            UsersMapper users = session.getMapper(UsersMapper.class);
            return users.findByUsername(username);
        }
    }
}
```

_At present_, `MybatisBundle` doesn't provide any kind of automated transaction
management. Applications are responsible for demarcating their own transactions
using the session's `commit()` and `rollback()` methods.
