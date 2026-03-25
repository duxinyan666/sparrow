# 贡献指南

感谢你对 JackSparrow 项目的关注！欢迎贡献代码、报告问题或提出建议。

## 🤝 如何贡献

### 1. Fork 项目

在 GitHub 上 fork 本项目到你的账户。

### 2. 克隆仓库

```bash
git clone https://github.com/YOUR_USERNAME/sparrow.git
cd sparrow
```

### 3. 创建分支

```bash
git checkout -b feature/your-feature-name
```

分支命名规范：
- `feature/xxx` - 新功能
- `bugfix/xxx` - Bug 修复
- `docs/xxx` - 文档更新
- `refactor/xxx` - 代码重构
- `test/xxx` - 测试相关

### 4. 开发并提交

```bash
# 修改代码
# ...

# 提交（遵循 Commit 规范）
git commit -m "feat: add your feature description"

# 推送
git push origin feature/your-feature-name
```

### 5. 创建 Pull Request

在 GitHub 上创建 PR，描述你的改动。

---

## 📝 Commit 规范

本项目遵循 [Conventional Commits](https://www.conventionalcommits.org/) 规范：

### 类型

- `feat`: 新功能
- `fix`: Bug 修复
- `docs`: 文档更新
- `style`: 代码格式（不影响功能）
- `refactor`: 重构
- `perf`: 性能优化
- `test`: 测试相关
- `chore`: 构建/工具/配置

### 格式

```
<type>(<scope>): <description>

[optional body]

[optional footer]
```

### 示例

```
feat(database): 添加 PostgreSQL 数据源支持

- 实现 PostgreSQLProvider
- 添加 HikariCP 连接池配置
- 编写单元测试

Closes #123
```

---

## 🧪 测试要求

### 单元测试

新功能必须包含单元测试：

```bash
# 运行所有测试
mvn test

# 运行特定测试
mvn test -Dtest=RedisServiceTest
```

### 测试覆盖率

目标覆盖率：> 80%

```bash
mvn test jacoco:report
```

---

## 📖 文档要求

### 代码注释

- 公共类和方法必须有 JavaDoc
- 复杂逻辑需要添加注释说明

### API 文档

- 新接口需要添加 Swagger 注解
- 更新 EXAMPLES.md 使用示例

### README 更新

- 新功能需要在 README.md 中说明
- 更新技术栈列表（如适用）

---

## 🔍 Code Review

### 检查清单

- [ ] 代码遵循项目风格
- [ ] 包含必要的单元测试
- [ ] 通过所有现有测试
- [ ] 文档已更新
- [ ] 无 IDE 警告
- [ ] 无敏感信息（密码、密钥等）

### 代码风格

- 使用 4 个空格缩进
- 类名：PascalCase
- 方法/变量：camelCase
- 常量：UPPER_SNAKE_CASE
- 行宽：不超过 120 字符

---

## 🐛 报告问题

### Bug 报告

请提供以下信息：

1. **环境信息**
   - Java 版本
   - 操作系统
   - JackSparrow 版本

2. **问题描述**
   - 期望行为
   - 实际行为
   - 复现步骤

3. **日志**
   - 错误堆栈
   - 相关日志

### 功能建议

请描述：

1. 功能用途
2. 使用场景
3. 预期效果

---

## 📐 架构原则

### 数据源扩展

添加新数据源类型：

1. 实现 `DataSourceProvider` 接口
2. 添加 `@MiddlewareType` 注解
3. 在 `dataSource.json` 中添加配置示例
4. 编写 Service 和 Controller（可选）
5. 添加单元测试

### API 设计

- RESTful 风格
- 统一响应格式（`Result` 类）
- 完整的 Swagger 文档
- 适当的错误处理

---

## 🚀 发布流程

### 版本命名

遵循 Semantic Versioning (SemVer)：

- `MAJOR.MINOR.PATCH`
- 例如：`1.2.3`

### 发布检查清单

- [ ] 更新版本号（pom.xml）
- [ ] 更新 CHANGELOG.md
- [ ] 运行所有测试
- [ ] 更新文档
- [ ] 创建 Git Tag
- [ ] 发布 GitHub Release

---

## 💬 讨论

- GitHub Issues: 问题和讨论
- Email: your-email@example.com

---

## 📜 许可证

MIT License - 详见 [LICENSE](LICENSE) 文件

---

感谢所有贡献者！🎉

<a href="https://github.com/duxinyan666/sparrow/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=duxinyan666/sparrow" />
</a>
