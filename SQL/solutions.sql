-- schema creation

create table departments (
    dept_id integer primary key,
    dept_name varchar(100) not null unique
);

create table employees (
    emp_id integer primary key,
    emp_name varchar(100) not null,
    dept_id integer references departments (dept_id),
    salary numeric(12, 2) not null check (salary >= 0),
    manager_id integer references employees (emp_id),
    join_date date not null
);

create table projects (
    project_id integer primary key,
    project_name varchar(100) not null,
    dept_id integer references departments (dept_id),
    budget numeric(14, 2) not null check (budget >= 0)
);

create table employeeprojects (
    emp_id integer references employees (emp_id),
    project_id integer references projects (project_id),
    hours_worked numeric(10, 2) not null check (hours_worked >= 0),
    primary key (emp_id, project_id)
);

create table orders (
    order_id integer primary key,
    customer_id integer not null,
    order_date date not null,
    amount numeric(14, 2) not null check (amount >= 0),
    status varchar(20) not null check (status in ('COMPLETED', 'PENDING', 'CANCELLED'))
);

create index employees_dept_idx on employees (dept_id);
create index orders_customer_date_idx on orders (customer_id, order_date);

-- sample data

insert into departments (dept_id, dept_name) values
    (10, 'Engineering'),
    (20, 'Sales'),
    (30, 'HR'),
    (40, 'Research'),
    (50, 'Empty Department');

insert into employees (emp_id, emp_name, dept_id, salary, manager_id, join_date) values
    (1, 'Alice', 10, 120000, null, '2021-01-10'),
    (2, 'Bob', 10, 90000, 1, '2022-02-01'),
    (3, 'Cara', 10, 90000, 1, '2022-03-01'),
    (4, 'Dan', 10, 70000, 1, '2023-04-01'),
    (5, 'Eva', 20, 80000, null, '2021-05-01'),
    (6, 'Finn', 20, 60000, 5, '2023-01-15'),
    (7, 'Gina', 20, 50000, 5, '2023-02-15'),
    (8, 'Hank', 30, 55000, null, '2020-06-20'),
    (9, 'Iris', 40, 110000, null, '2024-01-02'),
    (10, 'Jon', 40, 70000, 9, '2024-02-02');

insert into projects (project_id, project_name, dept_id, budget) values
    (101, 'Platform', 10, 500000),
    (102, 'Mobile', 10, 200000),
    (201, 'Pipeline', 20, 100000),
    (401, 'Lab', 40, 300000);

insert into employeeprojects (emp_id, project_id, hours_worked) values
    (1, 101, 40),
    (2, 101, 80),
    (3, 101, 80),
    (4, 101, 20),
    (2, 102, 30),
    (3, 102, 10),
    (5, 201, 70),
    (6, 201, 50),
    (9, 401, 100);

insert into orders (order_id, customer_id, order_date, amount, status) values
    (1, 101, '2026-01-01', 60000, 'COMPLETED'),
    (2, 101, '2026-02-01', 50000, 'COMPLETED'),
    (3, 101, '2026-03-01', 10000, 'PENDING'),
    (4, 102, '2026-01-15', 20000, 'COMPLETED'),
    (5, 102, '2026-02-15', 5000, 'COMPLETED'),
    (6, 103, '2026-04-10', 15000, 'COMPLETED'),
    (7, 103, '2026-06-10', 15000, 'COMPLETED'),
    (8, 103, '2026-05-10', 15000, 'COMPLETED'),
    (9, 104, '2026-01-03', 1000, 'CANCELLED'),
    (10, 105, '2026-02-20', 100000, 'COMPLETED'),
    (11, 106, '2026-02-21', 19999, 'COMPLETED');

-- question 1: department salary summary
-- include departments with no employees through a left join.

select
    d.dept_name,
    count(e.emp_id) as employee_count,
    avg(e.salary) as average_salary,
    min(e.salary) as minimum_salary,
    max(e.salary) as maximum_salary
from departments as d
left join employees as e using (dept_id)
group by d.dept_id, d.dept_name
order by d.dept_id;

-- question 2: second highest salary per department
-- dense_rank returns every employee at the second distinct salary.

with ranked as (
    select
        e.*,
        dense_rank() over (
            partition by dept_id
            order by salary desc
        ) as salary_rank
    from employees as e
)
select dept_id, emp_id, emp_name, salary
from ranked
where salary_rank = 2
order by dept_id, emp_id;

-- question 3: employees above department average
-- a partitioned window average avoids hard-coded departments.

select emp_id, emp_name, dept_id, salary, department_average
from (
    select
        e.*,
        avg(salary) over (partition by dept_id) as department_average
    from employees as e
) as averages
where salary > department_average
order by dept_id, salary desc;

-- question 4: manager versus team salary
-- retain managers with at least two direct reports.

select
    m.emp_id as manager_id,
    m.emp_name as manager_name,
    count(r.emp_id) as direct_report_count,
    sum(r.salary) as team_salary,
    m.salary as manager_salary
from employees as m
join employees as r on r.manager_id = m.emp_id
group by m.emp_id, m.emp_name, m.salary
having count(r.emp_id) >= 2
order by m.emp_id;

-- question 5: top project contributors
-- rank after aggregation so ties at rank two are retained.

with totals as (
    select
        p.project_id,
        p.project_name,
        e.emp_id,
        e.emp_name,
        sum(ep.hours_worked) as total_hours
    from projects as p
    join employeeprojects as ep using (project_id)
    join employees as e using (emp_id)
    group by p.project_id, p.project_name, e.emp_id, e.emp_name
), ranked as (
    select
        totals.*,
        rank() over (
            partition by project_id
            order by total_hours desc
        ) as contributor_rank
    from totals
)
select project_id, project_name, emp_id, emp_name, total_hours
from ranked
where contributor_rank <= 2
order by project_id, contributor_rank, emp_id;

-- question 6: departments without active projects
-- require employees and no project associated with the department.

select d.dept_id, d.dept_name
from departments as d
where exists (
    select 1
    from employees as e
    where e.dept_id = d.dept_id
)
and not exists (
    select 1
    from projects as p
    where p.dept_id = d.dept_id
)
order by d.dept_id;

-- question 7: running order revenue
-- aggregate completed orders per day before the cumulative window.

with daily as (
    select order_date, sum(amount) as daily_revenue
    from orders
    where status = 'COMPLETED'
    group by order_date
)
select
    order_date,
    daily_revenue,
    sum(daily_revenue) over (
        order by order_date
        rows between unbounded preceding and current row
    ) as cumulative_revenue
from daily
order by order_date;

-- question 8: customer order classification
-- classify each customer's completed-order total by the given thresholds.

with totals as (
    select customer_id, sum(amount) as completed_value
    from orders
    where status = 'COMPLETED'
    group by customer_id
)
select
    customer_id,
    completed_value,
    case
        when completed_value >= 100000 then 'PLATINUM'
        when completed_value >= 50000 then 'GOLD'
        when completed_value >= 20000 then 'SILVER'
        else 'BRONZE'
    end as customer_class
from totals
order by customer_id;

-- question 9: consecutive monthly orders
-- normalize distinct months and group consecutive calendar months.

with months as (
    select distinct
        customer_id,
        date_trunc('month', order_date)::date as month_start
    from orders
), grouped as (
    select
        customer_id,
        month_start,
        month_start - (
            row_number() over (
                partition by customer_id
                order by month_start
            )::integer * interval '1 month'
        ) as month_group
    from months
), runs as (
    select customer_id, month_group, count(*) as month_count
    from grouped
    group by customer_id, month_group
)
select distinct customer_id
from runs
where month_count >= 3
order by customer_id;

-- question 10: salary gap analysis
-- separate high and low ranks preserve ties and the department gap.

with stats as (
    select
        dept_id,
        min(salary) as lowest_salary,
        max(salary) as highest_salary
    from employees
    group by dept_id
), ranked as (
    select
        e.*,
        rank() over (partition by dept_id order by salary desc) as high_rank,
        rank() over (partition by dept_id order by salary asc) as low_rank
    from employees as e
)
select
    r.dept_id,
    r.emp_id,
    r.emp_name,
    case
        when r.high_rank = 1 and r.low_rank = 1 then 'HIGHEST_AND_LOWEST'
        when r.high_rank = 1 then 'HIGHEST'
        else 'LOWEST'
    end as salary_role,
    r.salary,
    s.highest_salary - s.lowest_salary as salary_difference
from ranked as r
join stats as s using (dept_id)
where r.high_rank = 1 or r.low_rank = 1
order by r.dept_id, r.salary desc, r.emp_id;
