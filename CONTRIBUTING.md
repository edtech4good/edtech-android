# Contributing to EdTech Android

Thank you for your interest in contributing to the EdTech Android project! This document provides guidelines and information for contributors.

## 🤝 How to Contribute

### Reporting Bugs

Before creating bug reports, please check the existing issues to see if the problem has already been reported. When creating a bug report, please include:

- A clear and descriptive title
- Steps to reproduce the issue
- Expected behavior
- Actual behavior
- Screenshots (if applicable)
- Environment information (OS, Android Studio version, API level)
- Any relevant error messages

### Suggesting Enhancements

If you have a suggestion for a new feature or improvement:

- Check if the feature has already been requested
- Provide a clear description of the proposed feature
- Explain why this feature would be useful
- Include mockups or examples if applicable

### Code Contributions

#### Setting Up Your Development Environment

1. Fork the repository
2. Clone your fork locally
3. Copy configuration files:
   ```bash
   cp local.properties.example local.properties
   cp app/google-services.example.json app/google-services.json
   cp buildspec.example.yml buildspec.yml
   ```
4. Configure the files with your settings
5. Create a new branch for your feature: `git checkout -b feature/your-feature-name`

#### Development Guidelines

- **Code Style**: Follow the existing code style and formatting
- **Java**: Use Java 8 or higher
- **Testing**: Write tests for new features and ensure existing tests pass
- **Documentation**: Update documentation for any new features or changes
- **Commits**: Use clear, descriptive commit messages

#### Pull Request Process

1. Ensure your code follows the project's coding standards
2. Update documentation if necessary
3. Add tests for new functionality
4. Ensure all tests pass
5. Update the README.md if you've changed the setup process
6. Submit a pull request with a clear description of the changes

### Code Review Process

- All pull requests require review before merging
- Reviewers will check for:
  - Code quality and style
  - Test coverage
  - Documentation updates
  - Security considerations
- Address any feedback from reviewers
- Once approved, your PR will be merged

## 📋 Development Standards

### Code Style

- Use 4 spaces for indentation
- Use camelCase for variables and methods
- Use PascalCase for classes
- Use UPPER_SNAKE_CASE for constants
- Add proper JavaDoc comments for public methods

### File Naming

- Use PascalCase for Java files
- Use snake_case for resource files
- Use descriptive names that indicate the purpose

### Class Structure

```java
package com.fortyk.studentapp.example;

import android.content.Context;
import android.util.Log;

/**
 * Example class demonstrating proper structure
 */
public class ExampleClass {
    private static final String TAG = "ExampleClass";
    
    private Context context;
    private String exampleProperty;
    
    public ExampleClass(Context context) {
        this.context = context;
    }
    
    /**
     * Example method with proper documentation
     * @param input The input parameter
     * @return The processed result
     */
    public String processInput(String input) {
        Log.d(TAG, "Processing input: " + input);
        return input.toUpperCase();
    }
}
```

## 🧪 Testing

### Running Tests

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Run all tests
./gradlew check
```

### Writing Tests

- Write unit tests for utility functions
- Write integration tests for API services
- Write UI tests for activities and fragments
- Aim for at least 80% code coverage

## 📚 Documentation

### Code Documentation

- Use JavaDoc comments for public methods and classes
- Include parameter types and return types
- Provide examples for complex methods

```java
/**
 * Calculates the sum of two numbers
 * @param a The first number
 * @param b The second number
 * @return The sum of a and b
 * @throws IllegalArgumentException if either parameter is null
 */
public int add(int a, int b) {
    return a + b;
}
```

### README Updates

- Update the README.md when adding new features
- Include setup instructions for new dependencies
- Update the project structure if you add new directories

## 🔒 Security

### Security Guidelines

- Never commit sensitive information (API keys, passwords, etc.)
- Use environment variables for configuration
- Validate all user inputs
- Follow security best practices for authentication
- Report security vulnerabilities privately

### Reporting Security Issues

If you discover a security vulnerability, please report it privately:

1. Do not create a public issue
2. Email the maintainers with details
3. Include steps to reproduce the vulnerability
4. Wait for acknowledgment before disclosing publicly

## 🏷️ Version Control

### Branch Naming

- `feature/feature-name` - New features
- `bugfix/bug-description` - Bug fixes
- `hotfix/urgent-fix` - Critical fixes
- `docs/documentation-update` - Documentation changes

### Commit Messages

Use conventional commit format:

```
type(scope): description

[optional body]

[optional footer]
```

Types:
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes
- `refactor`: Code refactoring
- `test`: Test changes
- `chore`: Build or tool changes

Examples:
```
feat(auth): add login functionality
fix(api): resolve timeout issue in data sync
docs(readme): update installation instructions
```

## 🎯 Getting Help

If you need help with contributing:

1. Check the existing documentation
2. Search existing issues and discussions
3. Create a new issue with the "question" label
4. Join our community discussions

## 📄 License

By contributing to this project, you agree that your contributions will be licensed under the MIT License.

## 🙏 Recognition

Contributors will be recognized in the project's README.md and release notes. Significant contributions may be eligible for maintainer status.

Thank you for contributing to EdTech Android! 🚀
