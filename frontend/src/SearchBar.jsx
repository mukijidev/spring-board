import { useState } from 'react'

function SearchBar ({onSearch}) {
    const [text, setText] = useState('')

    function handleSubmit(event) {
        event.preventDefault()
        onSearch(text.trim())
    }

    return (
        <form onSubmit={handleSubmit}>
            <input
                value={text}
                onChange={(event) => setText(event.target.value)}
                placeholder='Search titles'
            />
            <button type="submit">Search</button>
        </form>
    )
}

export default SearchBar