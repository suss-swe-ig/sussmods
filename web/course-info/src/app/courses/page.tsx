export default async function Page() {
    const response = await fetch('http://localhost:8080/courses/hello');
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    const result = await response.text(); // G
    return (
        <div>{result}</div>
    );
}